package io.monkeypatch.talks.waterpouring.server

import io.monkeypatch.talks.waterpouring.model.Move
import io.monkeypatch.talks.waterpouring.model.State
import io.monkeypatch.talks.waterpouring.model.moveModule
import io.monkeypatch.talks.waterpouring.model.toDisplayString
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.core.io.ClassPathResource
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.REQUEST_TIMEOUT
import org.springframework.web.reactive.function.server.*
import reactor.core.publisher.Mono
import java.net.URI
import java.time.Duration
import java.util.concurrent.TimeoutException


/**
 * The Spring boot application
 *
 * We need the verbose primary constructor syntax to allow adding annotation
 */
@SpringBootApplication
class ServerApplication(val solver: Solver) {

    /** A logger */
    internal val logger = LoggerFactory.getLogger(ServerApplication::class.java)

    /**
     * Get the solver timeout
     *
     * The value could be set in configuration (application.properties)
     */
    @Value("\${solver.timeout.in.seconds}")
    var timeoutInSeconds: Long = 10


    /**
     * Provide Jackson module for JSON Serialization/Deserialization,
     * handle [io.monkeypatch.talks.waterpouring.model.Move] sealed class
     */
    @Bean
    fun addJacksonModule() = moveModule

    /**
     * Define application routes:
     *
     * GET  /           : redirect to index.html
     * POST /api/solve  : solve the water pouring problem
     *      *           : serve static pages
     */
    @Bean
    fun routes() = router {
        GET("/") { ServerResponse.permanentRedirect(URI("/index.html")).build() }
        "/api".nest {
            POST("/solve", ::solve)
        }
        resources("/**", ClassPathResource("static/"))
    }

    /**
     * Handle exception
     */
    @Bean
    fun exceptionResponseMapper(): ExceptionResponseMapper = { error ->
        logger.error("Oops !", error)
        when (error) {
            is IllegalStateException -> BAD_REQUEST to (error.message ?: "Oops !")
            is TimeoutException      -> REQUEST_TIMEOUT to (error.message ?: "Timeout !")
            else                     -> null
        }
    }

    /**
     * Solve the water pouring problem
     *
     * @param request the [ServerRequest]
     * @return a [Mono] of [ServerResponse] with the result
     * @throws [TimeoutException] if too long to solve the problem
     * @throws [IllegalStateException] if there is a parsing issue, or if the problem is not solvable
     */
    internal fun solve(request: ServerRequest): Mono<ServerResponse> {
        val result = solveWithTimeout(request.bodyToMono()) // extract body
        return ServerResponse.ok().body(result)
    }

    /**
     * Reactive Solve the problem with a timeout delay
     *
     * @param input the input as a [Mono] of [Pair] of initial and final [State]
     * @return the result as [Mono] of [List] of [Move] or a failure if solve during more than [timeoutInSeconds]
     */
    internal fun solveWithTimeout(input: Mono<Pair<State, State>>): Mono<List<Move>> =
        input.doOnNext { (from, to) -> logger.info("Solve ${from.toDisplayString()} --> ${to.toDisplayString()}") }
            .map { (from, to) -> solver.solve(from, to) } // Solve
            .timeout(Duration.ofSeconds(timeoutInSeconds)) // Limit time

}