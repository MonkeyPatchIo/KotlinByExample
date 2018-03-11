package io.monkeypatch.talks.waterpouring.server

import com.fasterxml.jackson.databind.Module
import io.monkeypatch.talks.waterpouring.model.moveModule
import io.monkeypatch.talks.waterpouring.server.service.Solver
import io.monkeypatch.talks.waterpouring.server.service.TailRecursiveSolver
import io.monkeypatch.talks.waterpouring.server.utils.ApplicationWebExceptionHandler
import io.monkeypatch.talks.waterpouring.server.utils.CorsWebFilter
import io.monkeypatch.talks.waterpouring.server.utils.ExceptionResponseMapper
import io.monkeypatch.talks.waterpouring.server.utils.seconds
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler
import org.springframework.context.support.BeanDefinitionDsl
import org.springframework.context.support.beans
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.web.server.WebFilter
import java.time.Duration

/**
 * Define injectable beans
 *
 */
internal val beans: BeanDefinitionDsl =
    beans {
        // CORS
        bean<WebFilter> {
            CorsWebFilter(allowedOrigin = "*") // FIXME do something safer for production
        }

        // Custom JSON serialization/deserialization with Jackson
        bean<Module> {
            moveModule
        }

        // Custom exceptions handling
        bean<ErrorWebExceptionHandler> {
            ApplicationWebExceptionHandler(exceptionResponseMapper())
        }

        // Solver service
        bean<Solver> {
            TailRecursiveSolver()
        }

        // Routes
        bean {
            val timeout = getConfigurationTimeout(env)
            applicationRoutes(ref(), timeout)

        }
    }

/**
 * Get the "solver.timeout.in.seconds" property and create a [java.time.Duration]
 * Note: extracted as function for testing purpose, in real world application it should be inlined
 *
 * @param env the Spring environment
 */
internal fun getConfigurationTimeout(env: ConfigurableEnvironment): Duration =
    env.getProperty("solver.timeout.in.seconds", Int::class.java, 1).seconds

/**
 * Return the [ExceptionResponseMapper] (alias for [(Throwable) -> Pair<HttpStatus, String>?]) used by
 * a [ApplicationWebExceptionHandler] to handle custom response when an [Exception] occurs.
 *
 * @return a function that can provide a [Pair] of [HttpStatus] and [String] when we could map the exception
 */
internal fun exceptionResponseMapper(): ExceptionResponseMapper = { error ->
    TODO("3.3")
}

