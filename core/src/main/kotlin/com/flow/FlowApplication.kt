package com.flow

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FlowApplication

fun main(args: Array<String>) {
    runApplication<FlowApplication>(*args)
}
