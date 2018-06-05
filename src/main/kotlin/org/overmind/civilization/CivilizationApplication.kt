package org.overmind.civilization

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.reactive.HandlerMapping
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping
import org.springframework.web.reactive.socket.WebSocketHandler

@SpringBootApplication
class CivilizationApplication(val webSocketHandler: WebSocketHandler) {

    @Bean
    fun webSocketHandlerMapping(): HandlerMapping {
        return SimpleUrlHandlerMapping()
                .apply {
                    order = 1
                    urlMap = mapOf("/event-emitter" to webSocketHandler)
                }
    }
}

fun main(args: Array<String>) {
    runApplication<CivilizationApplication>(*args)
}