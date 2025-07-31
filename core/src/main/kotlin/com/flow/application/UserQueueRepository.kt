package com.flow.application

import reactor.core.publisher.Mono

interface UserQueueRepository {

    fun registerWaitQueue(queue: String, userId: Long): Mono<Long>

    fun allowUser(queue: String, count: Long): Mono<Long>

    fun isAllowed(queue: String, userId: Long): Mono<Boolean>
}