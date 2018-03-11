package io.monkeypatch.talks.waterpouring.server

import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import io.kotlintest.specs.ShouldSpec
import io.monkeypatch.talks.waterpouring.model.Empty
import io.monkeypatch.talks.waterpouring.model.Move
import io.monkeypatch.talks.waterpouring.model.toState
import io.monkeypatch.talks.waterpouring.server.service.Solver
import org.springframework.beans.factory.getBean
import org.springframework.context.support.GenericApplicationContext
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.http.HttpMethod.POST
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.OK
import org.springframework.http.HttpStatus.REQUEST_TIMEOUT
import org.springframework.mock.web.reactive.function.server.MockServerRequest
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.net.URI
import java.time.Duration
import java.util.concurrent.TimeoutException

class ApplicationSpec : ShouldSpec() {

    init {

        "3.1/ ServerApplication" {

            should("have a solver injected") {
                val context = GenericApplicationContext()
                beans.initialize(context)
                context.refresh()

                val solver = context.getBean<Solver>()

                solver shouldNotBe null
            }

            should("have a timeoutInSeconds read from configuration") {
                val env = mock<ConfigurableEnvironment>()
                whenCalling(env.getProperty("solver.timeout.in.seconds", Int::class.java)).thenReturn(42)
                whenCalling(env.getProperty("solver.timeout.in.seconds", Int::class.java, 1)).thenReturn(42)

                val timeout = getConfigurationTimeout(env)

                timeout shouldBe Duration.ofSeconds(42)
            }
        }

        "3.2/ ServerApplication_routes" {

            should("define a POST /api/solve route") {
                val solver = mock<Solver>()
                val routes: RouterFunction<ServerResponse> = applicationRoutes(solver)
                val mockRequest = MockServerRequest.builder()
                    .method(POST).uri(URI("/api/solve"))
                    .header("Content-Type", "application/json")
                    .build()

                val route = routes.route(mockRequest)

                StepVerifier.create(route)
                    .expectNextMatches { handler -> handler != null }
                    .expectComplete()
                    .verify()
            }

            should("call solve") {
                val initial = "0/5, 0/3".toState()
                val final = "4/5, 0/3".toState()
                val mockResult = listOf<Move>(Empty(0))
                val solver = mock<Solver>()
                whenCalling(solver.solve(initial, final)).thenReturn(mockResult)
                val routes = applicationRoutes(solver)
                val mockRequest = MockServerRequest.builder()
                    .method(POST).uri(URI("/api/solve"))
                    .header("Content-Type", "application/json")
                    .body(Mono.just(initial to final))

                val solution: Mono<ServerResponse> = routes.route(mockRequest)
                    .flatMap { it.handle(mockRequest) }

                StepVerifier.create(solution)
                    .expectNextMatches { it.statusCode() == OK }
                    .expectComplete()
                    .verify()
            }
        }

        "3.3/ ServerApplication_exception" {

            should("handle IllegalStateException") {
                val mapper = exceptionResponseMapper()
                val error = "Oops !"

                val (status, message) = mapper(IllegalStateException(error))
                        ?: throw AssertionError("Should be mapped")

                status shouldBe BAD_REQUEST
                message shouldBe error
            }

            should("handle TimeoutException") {
                val mapper = exceptionResponseMapper()
                val error = "Oops !"

                val (status, message) = mapper(TimeoutException(error))
                        ?: throw AssertionError("Should be mapped")

                status shouldBe REQUEST_TIMEOUT
                message shouldBe error
            }
        }

        "3.4/ ServerApplication_timeout" {

            should("send IllegalStateException if too long") {
                val timeout = Duration.ofSeconds(1)

                StepVerifier.withVirtualTime {
                    val mono = Mono.just(42)
                        .delayElement(timeout)
                        .doOnEach { println("Delayed: $it:") }
                    applyTimeout(mono, timeout)
                }
                    .thenAwait(timeout)
                    .verifyError(TimeoutException::class.java)
            }
        }
    }
}