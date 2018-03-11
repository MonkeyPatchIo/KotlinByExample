package io.monkeypatch.talks.waterpouring.web.test

import org.w3c.dom.HTMLElement
import org.w3c.dom.NodeList
import org.w3c.dom.get
import react.RBuilder
import react.dom.render
import kotlin.browser.document
import kotlin.js.Promise


/**
 * Helper to get template elements
 *
 * @return the template elements list
 */
fun templateElements(host: HTMLElement? = document.body,
                     block: RBuilder.() -> Unit): Promise<List<HTMLElement>> = Promise { resolve, reject ->
    render(container = host, callback = {
        val result = host?.childNodes?.asIterable()
            ?.toList()
        if (result != null) {
//            console.warn(result.joinToString("\n") { it.outerHTML })
            resolve(result)
        } else {
            reject(IllegalStateException("render fail"))
        }
    }, handler = block)
}

/**
 * Helper to get single template element
 *
 * @return the template element
 */
fun templateElement(host: HTMLElement? = document.body,
                    block: RBuilder.() -> Unit): Promise<HTMLElement> = Promise { resolve, reject ->
    render(container = host, callback = {
        val result = host?.get<HTMLElement>(0)
        if (result != null) {
//            console.warn(result.outerHTML)
            resolve(result)
        } else {
            reject(IllegalStateException("Render fail"))
        }
    }, handler = block)
}

/**
 * Get a child
 * @param index the child index
 * @return an [HTMLElement] is the child exists, and it has the good type
 */
inline operator fun <reified T : HTMLElement> HTMLElement.get(index: Int): T? =
    this.childNodes[index] as? T

/**
 * [NodeList] as [Iterable]
 */
fun NodeList.asIterable(): Iterable<HTMLElement> =
    (0 until (length))
        .map { get(it) }
        .filterIsInstance<HTMLElement>()
