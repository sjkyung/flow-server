package com.flow.interfaces

import com.flow.application.UserQueueService
import com.flow.support.ApiResponse
import org.springframework.http.ResponseCookie
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import java.time.Duration

@RestController
@RequestMapping("/api/v1/queues")
class UserQueueV1Controller(
    private val userQueueService: UserQueueService
): UserQueueV1ApiSpec {

    @PostMapping("")
    fun registerQueue(
        @RequestParam(name = "queue", defaultValue = "default") queue: String,
        @RequestParam(name = "userId") userId: Long,
    ): Mono<ApiResponse<UserQueueV1DTO.RegisterUserResponse>> {
        return userQueueService.registerWaitQueue(queue, userId)
            .map(UserQueueV1DTO.RegisterUserResponse::from)
            .map { ApiResponse.success(it) }
    }

    @PostMapping("/allow")
    fun allowUser(
        @RequestParam(name = "queue", defaultValue = "default") queue: String,
        @RequestParam(name = "count") count: Long
    ): Mono<ApiResponse<UserQueueV1DTO.AllowUserResponse>> {
        return userQueueService.allowUser(queue,count)
            .map{ allowed -> UserQueueV1DTO.AllowUserResponse.from(count, allowed)}
            .map{ ApiResponse.success(it) }
    }


    @GetMapping("/allowed")
    fun allowed(
        @RequestParam(name = "queue", defaultValue = "default") queue: String,
        @RequestParam(name = "userId") userId: Long
    ): Mono<ApiResponse<UserQueueV1DTO.AllowedUserResponse>> {
        return userQueueService.isAllowed(queue, userId)
            .map(UserQueueV1DTO.AllowedUserResponse::from)
            .map{ ApiResponse.success(it) }
    }

    @GetMapping("/rank")
    fun getRank(
        @RequestParam(name = "queue", defaultValue = "default") queue: String,
        @RequestParam(name = "userId") userId: Long
    ): Mono<ApiResponse<UserQueueV1DTO.RankNumberResponse>> {
        return userQueueService.getRank(queue, userId)
            .map(UserQueueV1DTO.RankNumberResponse::from)
            .map{ ApiResponse.success(it) }
    }


    @GetMapping("/touch")
    fun touch(
        @RequestParam(name = "queue", defaultValue = "default") queue: String,
        @RequestParam(name = "userId") userId: Long,
        exchange : ServerWebExchange
    ): Mono<ApiResponse<String>>{
        return Mono.defer {
            userQueueService.generateToken(queue, userId)
                .map { token ->
                    exchange.response.addCookie(
                        ResponseCookie
                            .from("user-queue-$queue-$userId", token)
                            .maxAge(Duration.ofSeconds(600)) // 쿠키 유효시간 10분 예시
                            .path("/")
                            .build()
                    )
                    token
                }.map { token -> ApiResponse.success(token) }
        }
    }

}
