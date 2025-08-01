package com.flow.interfaces.web

import com.flow.application.UserQueueService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.reactive.result.view.Rendering
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Controller
class WaitingRoomController(
    private val userQueueService: UserQueueService
) {


    @GetMapping("/waiting-room")
    fun waitingRoomPage(
        @RequestParam(name = "queue", defaultValue = "default") queue: String,
        @RequestParam(name = "user_id") userId: Long,
        @RequestParam(name = "redirect_url") redirectUrl: String,
        exchange : ServerWebExchange
    ): Mono<Rendering>{
        val key = "user-queue-$queue-$userId"
        val token = exchange.request.cookies.getFirst(key)?.value ?: ""

        //대기가 있는지 확인
        //없으면 타겟 페이지
        //있으면 대기 조회
        return userQueueService.isAllowedByToken(queue, userId, token)
            .filter { allowed -> allowed }
            .flatMap { Mono.just(Rendering.redirectTo(redirectUrl).build()) }
            .switchIfEmpty(
                userQueueService.registerWaitQueue(queue, userId)
                .onErrorResume { userQueueService.getRank(queue, userId)}
                .map { rank -> Rendering.view("waiting-room.html")
                    .modelAttribute("rank", rank)
                    .modelAttribute("userId", userId)
                    .modelAttribute("queue", queue)
                    .build()
                }
            )
    }
}