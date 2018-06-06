package org.overmind.civilization.server

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.core.ResolvableType
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.core.io.buffer.DataBufferFactory
import org.springframework.http.MediaType
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.http.codec.json.Jackson2JsonEncoder
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.toMono

@Service
class SumMapper(mapper: ObjectMapper) {

    private val encoder: Jackson2JsonEncoder = Jackson2JsonEncoder(mapper)

    private val decoder: Jackson2JsonDecoder = Jackson2JsonDecoder(mapper)

    fun encode(outbound: Flux<out Any>, dataBufferFactory: DataBufferFactory): Flux<DataBuffer> {
        return outbound.flatMap {
            encoder.encode(
                    it.toMono(),
                    dataBufferFactory,
                    ResolvableType.forType(Int::class.java),
                    MediaType.APPLICATION_JSON,
                    emptyMap()
            )
        }
    }

    fun decode(inbound: Flux<DataBuffer>): Flux<Sum> {
        return inbound.flatMap {
            decoder.decode(
                    it.toMono(),
                    ResolvableType.forClass(Sum::class.java),
                    MediaType.APPLICATION_JSON,
                    emptyMap()
            )
        }
                .map {
                    it as Sum
                }
    }

}