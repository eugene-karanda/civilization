package org.overmind.civilization

import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient
import org.springframework.web.reactive.socket.client.WebSocketClient
import reactor.core.publisher.Flux
import reactor.core.publisher.ReplayProcessor
import java.net.URI
import java.time.Duration


@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SumChannelTest {

    @LocalServerPort
    private lateinit var port: String

    private lateinit var client: WebSocketClient

    @Before
    fun setUp() {
        client = ReactorNettyWebSocketClient()
    }

    @Test
    fun shouldReturnSum() {
        val output = ReplayProcessor.create<Int>()

        client.execute(url("/stream")) { session ->
            session
                    .send(
                            Flux
                                    .just(
                                            "{\n  \"left\": 10,\n  \"right\": 7\n}",
                                            "{\n  \"left\": 1,\n  \"right\": -1\n}"
                                    )
                                    .map(session::textMessage)
                    )
                    .thenMany(
                            session.receive()
                                    .take(2)
                                    .map(WebSocketMessage::getPayloadAsText)
                                    .map(String::toInt)
                    )
                    .subscribeWith(output)
                    .then()
        }
                .block(Duration.ofSeconds(5))

        val results = output.collectList()
                .block(Duration.ofSeconds(5))

        Assertions.assertThat(results)
                .isEqualTo(listOf(17, 0))
    }

    private fun url(path: String) = URI("""ws://localhost:${this.port}$path""")
}