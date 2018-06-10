package org.overmind.civilization.server

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import java.util.*

@TypeAlias("game")
data class Game(
        val name: String,
        val owner: String,
        @Id val id: String = UUID.randomUUID().toString()
)

@Repository
interface GameRepository : ReactiveMongoRepository<Game, String>