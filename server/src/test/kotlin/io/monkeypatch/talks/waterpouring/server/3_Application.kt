@file:Suppress("ClassName")

package io.monkeypatch.talks.waterpouring.server

import io.monkeypatch.talks.waterpouring.model.Empty
import io.monkeypatch.talks.waterpouring.model.Glass
import io.monkeypatch.talks.waterpouring.model.Move
import org.amshove.kluent.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.mock.web.reactive.function.server.MockServerRequest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.net.URI
import java.time.Duration
import java.util.concurrent.TimeoutException


class ApplicationTests {


    @Nested
    @DisplayName("3.1/ ServerApplication")
    @RunWith(JUnitPlatform::class)
    @SpringBootTest
    @ExtendWith(SpringExtension::class)
    class ServerApplication_injection {

        @Autowired
        var application: ServerApplication? = null

        @Test
        fun `should have a solver injected`() {
            application?.solver.shouldNotBeNull()
        }

        @Test
        fun `should have a timeoutInSeconds read from configuration`() {
            val timeout = application?.timeoutInSeconds ?: 1
            timeout shouldEqual 42L
        }
    }

    @Nested
    @DisplayName("3.2/ ServerApplication_routes")
    class ServerApplication_routes {

        private val solver = mock<Solver>()
        private val application = ServerApplication(solver)

        @Test
        fun `should define a POST |api|solve route`() {
            val routes: RouterFunction<ServerResponse> = application.routes()
            val mockRequest = MockServerRequest.builder()
                .method(HttpMethod.POST)
                .uri(URI("/api/solve"))
                .header("Content-Type", "application/json")
                .build()

            val route = routes.route(mockRequest)

            StepVerifier.create(route)
                .expectNextMatches { handler -> handler != null }
                .expectComplete()
                .verify()
        }
    }

    @Nested
    @DisplayName("3.3/ ServerApplication_solve")
    class ServerApplication_solve {

        private val solver = mock<Solver>()
        private val application = ServerApplication(solver)

        @Test
        fun `should call solve`() {
            val initial = listOf(Glass(5), Glass(3))
            val final = listOf(Glass(5, 4), Glass(3))
            val mockResult = listOf<Move>(Empty(0))

            val mockRequest = MockServerRequest.builder()
                .method(HttpMethod.POST)
                .uri(URI("/api/solve"))
                .header("Content-Type", "application/json")
                .body(Mono.just(initial to final))

            When calling solver.solve(initial, final) itReturns mockResult

            val solution = application.solve(mockRequest)

            StepVerifier.create(solution)
                .expectNextMatches { it.statusCode() == HttpStatus.OK }
                .expectComplete()
                .verify()
        }
    }

    @Nested
    @DisplayName("3.4/ ServerApplication_exception")
    class ServerApplication_exception {

        private val solver = mock<Solver>()
        private val application = ServerApplication(solver)

        @Test
        fun `should handle IllegalStateException`() {
            val mapper = application.exceptionResponseMapper()
            val error = "Oops !"

            val (status, message) = mapper(IllegalStateException(error)) ?:
                    throw AssertionError("Should be mapped")

            status shouldEqual HttpStatus.BAD_REQUEST
            message shouldEqual error
        }

        @Test
        fun `should handle TimeoutException`() {
            val mapper = application.exceptionResponseMapper()
            val error = "Oops !"

            val (status, message) = mapper(TimeoutException(error)) ?:
                    throw AssertionError("Should be mapped")

            status shouldEqual HttpStatus.REQUEST_TIMEOUT
            message shouldEqual error
        }
    }


    @Nested
    @DisplayName("3.5/ ServerApplication_timeout")
    class ServerApplication_timeout {

        private val solver = mock<Solver>()
        private var application = ServerApplication(solver).apply {
            timeoutInSeconds = 1
        }

        @Test
        fun `should send IllegalStateException if too long`() {
            val initial = listOf(Glass(5), Glass(3))
            val final = listOf(Glass(5, 4), Glass(3))
            val input = Mono.just(initial to final)
            val timeout = Duration.ofSeconds(application.timeoutInSeconds)

            When calling solver.solve(initial, final) itReturns emptyList()

            val mono = application.solveWithTimeout(input.delayElement(timeout))
                .doOnEach { println("Delayed: $it:") }

            StepVerifier.withVirtualTime { mono }
                .verifyError(TimeoutException::class.java)
        }
    }


}