package com.flow.application

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface UserQueueRepository {

    fun registerWaitQueue(queue: String, userId: Long): Mono<Long>

    fun allowUser(queue: String, count: Long): Mono<Long>

    fun isAllowed(queue: String, userId: Long): Mono<Boolean>

    fun getRank(queue: String, userId: Long): Mono<Long>

    fun allowUsersInAllQueues(count: Long): Flux<Pair<String,Long>>
}