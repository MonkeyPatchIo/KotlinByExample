package io.monkeypatch.talks.waterpouring.web.test

import components.mainContainer
import model.Configuration
import model.UiState
import org.w3c.dom.Element
import org.w3c.dom.HTMLElement
import react.dom.render
import store.Store
import kotlin.browser.document
import kotlin.js.Promise


val config = object : Configuration {
    override val minCapacity = 1
    override val maxCapacity = 10
    override val url = ""
    override val debug = true
    override val host = ".main-test"
    override val initial = "0/5, 0/3"
    override val final = "4/5, 0/3"
}

private val host = document.querySelector(config.host)
val uiState = UiState(config)

fun <T> selectInMain(configuration: Configuration = config, store: Store<UiState>, selector: String) =
    Promise<Element> { resolve, reject ->
        val callback = {
            val elt = host?.querySelector(selector)
            elt shouldNotBeNull "element not found: '$selector'"

            if (elt != null) {
                resolve(elt)
            } else {
                reject(IllegalStateException("Render fail"))
            }
        }
        render(host, callback = callback) {
            mainContainer(configuration, store)
        }
    }

fun selectAllInMain(configuration: Configuration = config, store: Store<UiState>, selector: String) =
    Promise<Iterable<HTMLElement>> { resolve, reject ->
        val callback = {
            val child = host?.querySelectorAll(selector)

            if (child != null) {
                resolve(child.asIterable())
            } else {
                reject(IllegalStateException("Render fail"))
            }
        }

        render(host, callback = callback) {
            mainContainer(configuration, store)
        }
    }
