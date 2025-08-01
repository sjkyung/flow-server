package com.flow.infrastructure

import com.flow.application.UserQueueRepository
import com.flow.support.ApplicationException
import com.flow.support.ErrorCode
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.time.Instant

@Component
class UserQueueRepositoryImpl(
    private val reactiveRedisTemplate: ReactiveRedisTemplate<String, String>
) : UserQueueRepository {

    private val USER_QUEUE_WAIT_KEY = "users:queue:%s:wait"
    private val USER_QUEUE_PROCEEED_KEY = "users:queue:%s:proceed"

    override fun registerWaitQueue(queue: String, userId: Long): Mono<Long> {
        val unixTimestamp = Instant.now().epochSecond.toDouble()
        val key = USER_QUEUE_WAIT_KEY.format(queue)

        return reactiveRedisTemplate.opsForZSet()
            .add(key, userId.toString(), unixTimestamp)
            .filter { it }
            .switchIfEmpty(Mono.error(ApplicationException(ErrorCode.QUEUE_ALREADY_REGISTERED_USER)))
            .flatMap {
                reactiveRedisTemplate.opsForZSet().rank(key, userId.toString())
            }
            .map { if (it >= 0) it + 1 else it }
    }

    override fun allowUser(queue: String, count: Long): Mono<Long> {
        return reactiveRedisTemplate.opsForZSet().popMin(
                    USER_QUEUE_WAIT_KEY.format(queue),
                    count
                )
                .flatMap { member ->
                    reactiveRedisTemplate.opsForZSet().add(
                        USER_QUEUE_PROCEEED_KEY.format(queue),
                        member.value,
                        Instant.now().epochSecond.toDouble()
                    )
                }.count();
    }

    override fun isAllowed(queue: String, userId: Long): Mono<Boolean> {
        return reactiveRedisTemplate.opsForZSet().rank(
            USER_QUEUE_PROCEEED_KEY.format(queue),
            userId.toString()
        ).defaultIfEmpty(-1L)
        .map { rank -> rank >= 0 }
    }

    override fun getRank(queue: String, userId: Long): Mono<Long> {
        return reactiveRedisTemplate.opsForZSet().rank(
            USER_QUEUE_WAIT_KEY.format(queue),
            userId.toString()
        ).defaultIfEmpty(-1L)
        .map { rank -> if(rank >= 0) rank +1 else rank }
    }

}