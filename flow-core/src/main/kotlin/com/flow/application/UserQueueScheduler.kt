package com.flow.application

import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class UserQueueScheduler(
    private val userQueueService: UserQueueService,
    @Value("\${scheduler.enabled:false}")
    private val scheduling: Boolean
) {

    private val log = org.slf4j.LoggerFactory.getLogger(this::class.java)
    private val maxAllowUserCount = 100L

    @Scheduled(initialDelay = 5000,fixedDelay = 10000)
    fun shouldAllowUser() {
        if(!scheduling){
            log.info("passed Scheduler")
            return
        }
        log.info("called Scheduler")

        userQueueService.allowUsersInAllQueues(maxAllowUserCount)
            .doOnNext { (queue, allowed) ->
                log.info("Tried $maxAllowUserCount and allowed $allowed members of $queue queue")}
            .subscribe()
    }
}