package io.monkeypatch.talks.waterpouring.server.utils

import io.netty.handler.codec.http.HttpMethod.OPTIONS
import org.springframework.http.HttpStatus
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

internal class CorsWebFilter(val allowedOrigin: String) : WebFilter {
    override fun filter(ctx: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val isOption = ctx.request.method == OPTIONS

        // Add headers
        ctx.response.headers.apply {
            add("Access-Control-Allow-Origin", allowedOrigin)
            add("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE, OPTIONS")
            add("Access-Control-Allow-Headers",
                "DNT,X-CustomHeader,Keep-Alive,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,Content-Range,Range")

            if (isOption) {
                add("Access-Control-Max-Age", "1728000")
            } else {
                add("Access-Control-Expose-Headers",
                    "DNT,X-CustomHeader,Keep-Alive,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,Content-Range,Range")
            }
        }

        // Forward
        return if (isOption) {
            ctx.response.statusCode = HttpStatus.NO_CONTENT
            Mono.empty()
        } else {
            chain.filter(ctx)
        }
    }
}