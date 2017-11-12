package plop

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PlopApplication

fun main(args: Array<String>) {
    runApplication<PlopApplication>(*args)
}