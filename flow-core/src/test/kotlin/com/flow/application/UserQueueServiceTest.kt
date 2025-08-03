package com.flow.application

import com.flow.EmbeddedRedis
import com.flow.support.ApplicationException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.data.redis.connection.ReactiveRedisConnection
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.test.context.ActiveProfiles
import reactor.test.StepVerifier

@SpringBootTest
@Import(EmbeddedRedis::class)
@ActiveProfiles("test")
class UserQueueServiceTest @Autowired constructor(
    private val userQueueService: UserQueueService,
    private val userQueueRepository: UserQueueRepository,
    private val reactiveRedisTemplate: ReactiveRedisTemplate<String, String>
) {

    @BeforeEach
    fun beforeEach() {
        val redisConnection: ReactiveRedisConnection = reactiveRedisTemplate.connectionFactory.reactiveConnection
        StepVerifier.create(redisConnection.serverCommands().flushAll())
            .expectNextCount(1)
            .verifyComplete()
    }



    @Test
    fun registerWaitQueue() {
        StepVerifier.create(userQueueService.registerWaitQueue("default", 100))
            .expectNext(1L)
            .verifyComplete()

        StepVerifier.create(userQueueService.registerWaitQueue("default", 101))
            .expectNext(2L)
            .verifyComplete()

        StepVerifier.create(userQueueService.registerWaitQueue("default", 102))
            .expectNext(3L)
            .verifyComplete()
    }

    @Test
    fun alreadyRegisterWaitQueue(){
        StepVerifier.create(userQueueService.registerWaitQueue("default", 100))
            .expectNext(1L)
            .verifyComplete()

        StepVerifier.create(userQueueService.registerWaitQueue("default", 100))
            .expectError(ApplicationException::class.java)
            .verify()
    }

    @Test
    fun emptyAllowUser() {
        StepVerifier.create(userQueueService.allowUser("default", 3))
            .expectNext(0L)
            .verifyComplete()
    }


    @Test
    fun allowUser() {
        StepVerifier.create(userQueueService.registerWaitQueue("default", 100)
                .then(userQueueService.registerWaitQueue("default", 101))
                .then(userQueueService.registerWaitQueue("default", 102))
                .then(userQueueService.allowUser("default", 5)))
            .expectNext(3L)
            .verifyComplete()
    }


    @Test
    fun allowUserAfterRegisterWaitQueue() {
        StepVerifier.create(userQueueService.registerWaitQueue("default", 100)
            .then(userQueueService.registerWaitQueue("default", 101))
            .then(userQueueService.registerWaitQueue("default", 102))
            .then(userQueueService.allowUser("default", 3))
            .then(userQueueService.registerWaitQueue("default", 200)))
            .expectNext(1L)
            .verifyComplete()
    }


    @Test
    fun isNotAllowed() {
        StepVerifier.create(userQueueService.isAllowed("default", 100))
            .expectNext(false)
            .verifyComplete()
    }


    @Test
    fun allowAfterNotAllowed() {
        StepVerifier.create(userQueueService.registerWaitQueue("default", 100)
            .then(userQueueService.allowUser("default", 1))
            .then(userQueueService.isAllowed("default", 101)))
            .expectNext(false)
            .verifyComplete()
    }

    @Test
    fun isAllowed() {
        StepVerifier.create(userQueueService.registerWaitQueue("default", 100)
            .then(userQueueService.allowUser("default", 1))
            .then(userQueueService.isAllowed("default", 100)))
            .expectNext(true)
            .verifyComplete()
    }

    @Test
    fun isNotAllowedByToken() {
        StepVerifier.create(userQueueService.isAllowedByToken("default", 100, ""))
            .expectNext(false)
            .verifyComplete()
    }


    @Test
    fun isAllowedByToken() {
        StepVerifier.create(userQueueService.isAllowedByToken("default",
            100,
            "d333a5d4eb24f3f5cdd767d79b8c01aad3cd73d3537c70dec430455d37afe4b8")
        )
            .expectNext(true)
            .verifyComplete()
    }


    @Test
    fun emptyGetRank() {
        StepVerifier.create(userQueueService.getRank("default", 1000))
            .expectNext(-1)
            .verifyComplete()
    }


    @Test
    fun getRank() {
        StepVerifier.create(userQueueService.registerWaitQueue("default", 100)
            .then(userQueueService.registerWaitQueue("default", 101))
            .then(userQueueService.registerWaitQueue("default", 102))
            .then(userQueueService.getRank("default", 101)))
            .expectNext(2L)
            .verifyComplete()
    }


    @Test
    fun generateToken() {
        StepVerifier.create(userQueueService.generateToken("default", 100))
            .expectNext("d333a5d4eb24f3f5cdd767d79b8c01aad3cd73d3537c70dec430455d37afe4b8")
            .verifyComplete()
    }

}