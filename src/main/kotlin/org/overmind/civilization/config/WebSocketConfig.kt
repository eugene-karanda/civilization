package org.overmind.civilization.config

import org.overmind.civilization.SumChannel
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.HandlerMapping
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter

@Configuration
class WebSocketConfig {

    @Bean
    fun webSocketHandlerAdapter() = WebSocketHandlerAdapter()

    @Bean
    fun webSocketHandler(sumChannel: SumChannel): HandlerMapping {
        return SimpleUrlHandlerMapping()
                .apply {
                    order = 0
                    urlMap = mapOf("/stream" to sumChannel)
                }
    }
}