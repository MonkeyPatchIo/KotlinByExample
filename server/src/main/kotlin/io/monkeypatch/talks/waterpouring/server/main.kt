@file:JvmName("Main")

package io.monkeypatch.talks.waterpouring.server


import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


/**
 * The Spring boot application
 */
@SpringBootApplication
class ServerApplication

/**
 * Run SpringBoot server,
 * See [SpringApplication]
 */
fun main(args: Array<String>) {
    runApplication<ServerApplication>(*args) {
        // Add beans in the application context
        addInitializers(beans)
    }
}