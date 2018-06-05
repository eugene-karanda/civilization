package org.overmind.civilization

import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

@Component
class SumChannel(val mapper: SumMapper) : WebSocketHandler {

    override fun handle(session: WebSocketSession): Mono<Void> {
        return session.receive()
                .map(WebSocketMessage::retain)
                .map(WebSocketMessage::getPayload)
                .publishOn(Schedulers.parallel())
                .transform(mapper::decode)
                .transform(::process)
                .onBackpressureBuffer()
                .transform {
                    mapper.encode(it, session.bufferFactory())
                }
                .map {
                    WebSocketMessage(WebSocketMessage.Type.TEXT, it)
                }
                .`as`(session::send)
    }

    fun process(inbound: Flux<Sum>):Flux<Int> {
        return inbound.map {
            with(it) {
                left + right
            }
        }
    }
}

