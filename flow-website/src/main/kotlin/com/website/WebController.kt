package com.website

import org.slf4j.LoggerFactory;
import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder


@Controller
@RequestMapping("")
class WebController(
    private val restTemplate: RestTemplate = RestTemplate()
) {

    private val log =  LoggerFactory.getLogger(WebController::class.java)

    @GetMapping("")
    fun index(
        @RequestParam(name = "queue", defaultValue = "default") queue: String,
        @RequestParam(name = "userId") userId: Long,
        request: HttpServletRequest
    ): String {
        val cookies = request.cookies
        val cookieName = "user-queue-$queue-token"

        val token = cookies
            ?.firstOrNull { it.name.equals(cookieName, ignoreCase = true) }
            ?.value ?: ""



        val uri = UriComponentsBuilder.fromUriString("http://localhost:8600")
            .path("/api/v1/queues/allowed")
            .queryParam("queue", queue)
            .queryParam("userId", userId)
            .queryParam("token", token)
            .encode()
            .build()
            .toUri()


        val response = restTemplate.exchange(
            uri,
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<ApiResponse<AllowedUserResponse>>() {}
        )

        if (response.body?.data?.allowed == true) {
            val redirectUrl = "http://127.0.0.1:8500?userId=$userId"
            val waitRoomUrl =
                "http://localhost:8600/waiting-room?queue=$queue&user_id=$userId&redirect_url=$redirectUrl"
            return "redirect:$waitRoomUrl"
        }
        return "index"
    }

}