package io.monkeypatch.talks.waterpouring.server

import io.monkeypatch.talks.waterpouring.model.State
import io.monkeypatch.talks.waterpouring.server.service.Solver
import org.springframework.core.io.ClassPathResource
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.ServerResponse.permanentRedirect
import reactor.core.publisher.Mono
import java.net.URI
import java.time.Duration

/**
 * Define application routing
 *
 * See [Blog entry](https://spring.io/blog/2017/08/01/spring-framework-5-kotlin-apis-the-functional-way#functional-routing-with-the-kotlin-dsl-for-spring-webflux)
 */
internal fun applicationRoutes(solver: Solver,
                               timeout: Duration = Duration.ofSeconds(1)): RouterFunction<ServerResponse> =
    router {
        // Serve index.html on '/'
        GET("/") {
            permanentRedirect(URI("/index.html")).build()
        }

        // Solve the water pouring problem
        // TODO 3.4
        POST("/api/solve") { request ->
            val input = request.bodyToMono<Pair<State, State>>()
            val result = input.map { (from, to) -> solver.solve(from, to) }
            ServerResponse.ok().body(result)
        }

        // Serve static files
        resources("/**", ClassPathResource("static/"))
    }

/**
 * Apply a timeout to a Mono
 * Note: extracted as function for testing purpose, in real world application it should be inlined
 *
 * @param mono the  [Mono]
 * @param timeout the timeout
 * @return the result as [Mono] or a failure [timeout] exceeded
 */
internal fun <T> applyTimeout(mono: Mono<T>, timeout: Duration): Mono<T> =
    TODO("3.4")