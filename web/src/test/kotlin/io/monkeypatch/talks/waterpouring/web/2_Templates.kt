package io.monkeypatch.talks.waterpouring.web

import components.finalState
import components.glass
import components.initialState
import io.monkeypatch.talks.waterpouring.model.toGlass
import io.monkeypatch.talks.waterpouring.web.test.shouldBeGlass
import io.monkeypatch.talks.waterpouring.web.test.shouldBeTag
import io.monkeypatch.talks.waterpouring.web.test.shouldContainElement
import io.monkeypatch.talks.waterpouring.web.test.shouldHasClass
import io.monkeypatch.talks.waterpouring.web.test.shouldHaveMax
import io.monkeypatch.talks.waterpouring.web.test.shouldHaveMin
import io.monkeypatch.talks.waterpouring.web.test.shouldHaveSize
import io.monkeypatch.talks.waterpouring.web.test.shouldHaveText
import io.monkeypatch.talks.waterpouring.web.test.shouldHaveValue
import io.monkeypatch.talks.waterpouring.web.test.templateElement
import io.monkeypatch.talks.waterpouring.web.test.templateElements
import mocha.async
import mocha.describe
import mocha.invoke
import mocha.it
import model.Configuration
import model.UiState
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLSpanElement
import kotlin.browser.document

@Suppress("UnsafeCastFromDynamic")
val testTemplates = describe("[2] Templates") {
    val config = object : Configuration {
        override val minCapacity = 1
        override val maxCapacity = 10
        override val url = ""
        override val debug = true
        override val host = ".main-test"
        override val initial = "0/5, 0/3"
        override val final = "4/5, 0/3"
    }
    val host = document.querySelector(config.host) as HTMLElement?

    it.async("1. should render a Glass") { done ->
        val g = "2/10".toGlass()

        templateElement(host = host) {
            glass(g)
        }
            .then { node ->
                node shouldBeGlass g
                done()
            }
            .catch { done(it) }
    }

    it.async("2. should render final state") { done ->
        val uiState = UiState(config)
        val state = uiState.finalState

        templateElements(host = host) {
            finalState(state, onFinalCurrentChange = { _, _ -> })
        }
            .then { nodes ->
                nodes shouldHaveSize state.size

                nodes.zip(state).forEach { (node, glass) ->
                    // container
                    node shouldBeTag "div"
                    node shouldHasClass "glass-container"

                    // children
                    val glassNode: HTMLDivElement = node shouldContainElement ".glass"
                    glassNode shouldBeGlass glass

                    val input: HTMLInputElement = node shouldContainElement "div .current"
                    input shouldBeTag "input"
                    input shouldHaveValue "${glass.current}"
                    input shouldHaveMin 0
                    input shouldHaveMax glass.capacity

                    val span: HTMLSpanElement = node shouldContainElement "div .capacity"
                    span shouldBeTag "span"
                    span shouldHaveText "${glass.capacity}"
                }
                done()
            }
            .catch { done(it) }
    }

    it.async("3. should render initial state") { done ->
        val uiState = UiState(config)
        val state = uiState.initialState

        templateElements(host = host) {
            initialState(state,
                         minCapacity = config.minCapacity,
                         maxCapacity = config.maxCapacity,
                         onInitialCurrentChange = { _, _ -> },
                         onInitialCapacityChange = { _, _ -> })
        }
            .then { nodes ->
                nodes shouldHaveSize state.size

                nodes.zip(state).forEach { (node, glass) ->
                    // container
                    node shouldBeTag "div"
                    node shouldHasClass "glass-container"

                    // children
                    val glassNode: HTMLDivElement = node shouldContainElement ".glass"
                    glassNode shouldBeGlass glass

                    val inputCurrent: HTMLInputElement = node shouldContainElement "div .current"
                    inputCurrent shouldBeTag "input"
                    inputCurrent shouldHaveValue "${glass.current}"
                    inputCurrent shouldHaveMin 0
                    inputCurrent shouldHaveMax glass.capacity

                    val span: HTMLSpanElement = node shouldContainElement "div span"
                    span shouldBeTag "span"
                    span shouldHaveText "/"

                    val inputCapacity: HTMLInputElement = node shouldContainElement "div .capacity"
                    inputCapacity shouldBeTag "input"
                    inputCapacity shouldHaveValue "${glass.capacity}"
                    inputCapacity shouldHaveMin config.minCapacity
                    inputCapacity shouldHaveMax config.maxCapacity
                }
                done()
            }
            .catch { done(it) }
    }
}
