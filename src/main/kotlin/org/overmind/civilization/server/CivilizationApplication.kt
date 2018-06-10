package org.overmind.civilization.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CivilizationApplication

fun main(args: Array<String>) {
    runApplication<CivilizationApplication>(*args)
}