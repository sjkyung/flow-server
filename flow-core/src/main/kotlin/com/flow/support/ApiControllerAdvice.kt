package com.flow.support

import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import reactor.core.publisher.Mono

@RestControllerAdvice
class ApiControllerAdvice {

    private val log = LoggerFactory.getLogger(ApiControllerAdvice::class.java)

    @ExceptionHandler(ApplicationException::class)
    fun handleApplicationException(e: ApplicationException): Mono<ResponseEntity<ApiResponse<Any?>>> {
        log.warn("ApplicationException: {}", e.customMessage ?: e.message, e)
        return Mono.just(
            ResponseEntity
            .status(e.errorCode.httpStatus.value())
            .body(ApiResponse.fail(e.errorCode.code, e.customMessage ?: e.errorCode.message)))
    }

}