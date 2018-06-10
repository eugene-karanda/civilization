package org.overmind.civilization.server.api

import org.overmind.civilization.server.Game
import org.overmind.civilization.server.GameRepository
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("game")
class GameController(val gameRepository: GameRepository) {

    @GetMapping
    fun findAll(): Flux<Game> {
        return gameRepository.findAll()
    }

    @GetMapping("{id}")
    fun findOne(@PathVariable("id") id: String): Mono<Game> {
        return gameRepository.findById(id)
    }

    @PostMapping("create")
    fun create(@RequestBody gameCreationRequest: GameCreationRequest): Mono<Game> {
        val game = gameCreationRequest.toGame()
        return gameRepository.save(game)
    }
}

data class GameCreationRequest(val name: String, val owner: String)

fun GameCreationRequest.toGame() = Game(name, owner)