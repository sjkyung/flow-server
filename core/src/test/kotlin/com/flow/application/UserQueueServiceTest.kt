package com.flow.application

import com.flow.EmbeddedRedis
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import reactor.test.StepVerifier

@SpringBootTest
@Import(EmbeddedRedis::class)
@ActiveProfiles("test")
class UserQueueServiceTest @Autowired constructor(
    private val userQueueService: UserQueueService,
    private val userQueueRepository: UserQueueRepository
) {

    @Test
    fun registerWaitQueue() {
        StepVerifier.create(userQueueService.registerWaitQueue("default", 1))
            .expectNext(5L)
            .verifyComplete()

        StepVerifier.create(userQueueService.registerWaitQueue("default", 2))
            .expectNext(3L)
            .verifyComplete()

        StepVerifier.create(userQueueService.registerWaitQueue("default", 3))
            .expectNext(4L)
            .verifyComplete()
    }

    @Test
    fun alreadyRegisterWaitQueue(){
        StepVerifier.create(userQueueService.registerWaitQueue("default", 1))
            .expectNext(5L)
            .verifyComplete()

        StepVerifier.create(userQueueService.registerWaitQueue("default", 1))
            .expectErrorMatches { it.message == "이미 대기열에 등록된 유저입니다." }
            .verify()
    }

    @Test
    fun allowUser() {

    }

    @Test
    fun isAllowed() {

    }

}