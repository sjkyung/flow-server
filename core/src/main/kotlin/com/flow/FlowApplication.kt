package com.flow

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class FlowApplication

fun main(args: Array<String>) {
    runApplication<FlowApplication>(*args)
}
