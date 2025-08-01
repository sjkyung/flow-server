package com.flow.application

import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class UserQueueService(
    private val userQueueRepository: UserQueueRepository
) {
    //대기열에 등록
    fun registerWaitQueue(queue: String, userId: Long): Mono<Long> {
        return userQueueRepository.registerWaitQueue(queue, userId);
    }

    //진입이 가능한 상태인지 조회 -> 대기열에서 제거, 진입을 허용
    fun allowUser(queue: String, count: Long): Mono<Long> {
        return userQueueRepository.allowUser(queue, count);
    }

    //해당 유저가 진입 가능한 상태인지 조회
    fun isAllowed(queue: String, userId: Long): Mono<Boolean> {
        return userQueueRepository.isAllowed(queue, userId);
    }

    //해당 유저의 몇번째 순번인지 조회
    fun getRank(queue: String, userId: Long): Mono<Long> {
        return userQueueRepository.getRank(queue, userId);
    }


    fun allowUsersInAllQueues(maxAllowUserCount: Long): Flux<Pair<String,Long>> {
        return userQueueRepository.allowUsersInAllQueues(maxAllowUserCount);
    }

}