@file:JvmName("Main")

package io.monkeypatch.talks.waterpouring.server


import org.springframework.boot.SpringApplication
import org.springframework.boot.runApplication


/**
 * Run SpringBoot server,
 * See [SpringApplication]
 */
fun main(args: Array<String>) {
    runApplication<ServerApplication>(*args)
}