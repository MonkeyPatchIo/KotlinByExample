package helpers

import org.w3c.fetch.CORS
import org.w3c.fetch.DEFAULT
import org.w3c.fetch.ERROR
import org.w3c.fetch.Headers
import org.w3c.fetch.OMIT
import org.w3c.fetch.RequestCache
import org.w3c.fetch.RequestCredentials
import org.w3c.fetch.RequestInit
import org.w3c.fetch.RequestMode
import org.w3c.fetch.RequestRedirect
import org.w3c.fetch.Response
import kotlin.browser.window
import kotlin.js.Promise

/**
 * Extract JSON result of a [Response],
 * if the response is not ok, send an [Error]
 */
private fun <T> Promise<Response>.toJson(): Promise<T> =
    this.then { response ->
        if (response.ok) response.text()
        else response.text().then { error ->
            throw Error("[${response.status}]: ${response.statusText}\n$error")
        }
    }.then { JSON.parse<T>(it) }

/**
 * Make an HTTP GET request using [fetch](https://developer.mozilla.org/en-US/docs/Web/API/Fetch_API)
 *
 * @param url the url
 * @return a [Promise] with the parsed result
 */
fun <O> getJson(url: String): Promise<O> =
    window.fetch(url).toJson()

/**
 * Make an HTTP POST request with body formatted as JSON using [fetch](https://developer.mozilla.org/en-US/docs/Web/API/Fetch_API)
 *
 * @param url the url
 * @param input the input, serialized as JSON with [JSON.stringify]
 * @return a [Promise] with the result parsed
 */
fun <O> postJson(url: String, input: Any?): Promise<O> {
    val headers = Headers().apply {
        set("Content-Type", "application/json")
    }
    val init = RequestInit(method = "POST",
                           headers = headers,
                           mode = RequestMode.CORS,
                           referrerPolicy = "no-referrer",
                           integrity = "",
                           cache = RequestCache.DEFAULT,
                           credentials = RequestCredentials.OMIT,
                           redirect = RequestRedirect.ERROR,
                           body = JSON.stringify(input))
    return window.fetch(url, init).toJson()
}
