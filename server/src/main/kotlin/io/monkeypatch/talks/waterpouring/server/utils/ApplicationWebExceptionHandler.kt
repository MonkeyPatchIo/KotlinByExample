package io.monkeypatch.talks.waterpouring.server.utils

import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.TEXT_PLAIN
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebExceptionHandler
import reactor.core.publisher.Mono

/**
 * Define an [ExceptionResponseMapper],
 * Provide the [HttpStatus] and the message (the body) of an HTTP response
 */
typealias ExceptionResponseMapper = (Throwable) -> Pair<HttpStatus, String>?

/**
 * Define a [WebExceptionHandler] based on a [ExceptionResponseMapper]
 *
 * The `@Component` annotation allow dependency injection with this class
 * if there is an injectable mapper
 *
 * @property mapper the mapper
 */
class ApplicationWebExceptionHandler(private val mapper: ExceptionResponseMapper) : ErrorWebExceptionHandler {

    /**
     * Send with specific [HttpStatus] and a body as [String]
     */
    private fun ServerHttpResponse.send(status: HttpStatus, stringBody: String): Mono<Void> {
        val body = bufferFactory().wrap(stringBody.toByteArray())
        statusCode = status
        return writeWith(Mono.just(body))
    }

    /**
     * Handle exception
     */
    override fun handle(exchange: ServerWebExchange, ex: Throwable): Mono<Void> {
        val (status, message) = mapper(ex) ?: return Mono.empty()
        val response = exchange.response

        response.headers.contentType = TEXT_PLAIN
        return response.send(status, message)
    }
}