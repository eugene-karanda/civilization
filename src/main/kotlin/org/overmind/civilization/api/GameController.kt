package org.overmind.civilization.api

import org.overmind.civilization.Game
import org.overmind.civilization.GameRepository
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("game")
class GameController(val gameRepository: GameRepository) {

    @PostMapping("save")
    fun save(@RequestBody gameCreationRequest: GameCreationRequest): Mono<Game> {
        val game = gameCreationRequest.toGame()
        return gameRepository.save(game)
    }
}

data class GameCreationRequest(val name: String)

fun GameCreationRequest.toGame() = Game(name)