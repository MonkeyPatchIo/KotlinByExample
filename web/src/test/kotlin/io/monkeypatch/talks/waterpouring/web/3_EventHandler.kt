package io.monkeypatch.talks.waterpouring.web

import io.monkeypatch.talks.waterpouring.web.test.MockStore
import io.monkeypatch.talks.waterpouring.web.test.config
import io.monkeypatch.talks.waterpouring.web.test.selectAllInMain
import io.monkeypatch.talks.waterpouring.web.test.selectInMain
import io.monkeypatch.talks.waterpouring.web.test.shouldHaveAction
import io.monkeypatch.talks.waterpouring.web.test.shouldMatch
import io.monkeypatch.talks.waterpouring.web.test.uiState
import mocha.async
import mocha.describe
import mocha.invoke
import mocha.it
import model.UiState
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import redux.AddGlassAction
import redux.RemoveGlassAction
import redux.SetGlassCapacity
import redux.SetGlassCurrent
import redux.createReducer

@Suppress("UnsafeCastFromDynamic")
val testEvents = describe("[3] Event handlers") {

    it.async("1. should trigger remove glass") { done ->
        val mockStore = MockStore(uiState, createReducer())

        selectInMain<Any>(store = mockStore, selector = ".btn-remove")
            .then { button ->
                (button as? HTMLButtonElement)?.click()
                mockStore.shouldHaveAction<UiState, RemoveGlassAction>()
                done()
            }
            .catch { done(it) }
    }

    it.async("2. should trigger add glass") { done ->
        val mockStore = MockStore(uiState, createReducer())

        selectInMain<Any>(store = mockStore, selector = ".btn-add")
            .then { button ->
                (button as? HTMLButtonElement)?.click()
                mockStore.shouldHaveAction<UiState, AddGlassAction>()
                done()
            }
            .catch { done(it) }
    }

    it.skip.async("3. should update final current") { done ->
        val mockStore = MockStore(uiState, createReducer())
        val glassIndex = 0

        selectAllInMain(store = mockStore, selector = ".final.state .current")
            .then { inputs ->

                val glass = uiState.finalState[glassIndex]
                val newValue = if (glass.capacity == glass.current) 0 else glass.capacity

                val input = inputs.toList()[glassIndex]

                (input as? HTMLInputElement)?.apply {
                    value = newValue.toString()

                    dispatchEvent(Event("change"))
                    val action = mockStore.shouldHaveAction<UiState, SetGlassCurrent>()
                    action.shouldMatch {
                        val (isInitial, index, quantity) = it.payload
                        !isInitial && (index == glassIndex) && (quantity == newValue)
                    }
                }
                done()
            }
            .catch { done(it) }
    }

    it.skip.async("4. should update initial current") { done ->
        done()
        val mockStore = MockStore(uiState, createReducer())
        val glassIndex = 0
        selectAllInMain(store = mockStore, selector = "#init-current-$glassIndex")
            .then { inputs ->

                val glass = uiState.finalState[glassIndex]
                val newValue = if (glass.capacity == glass.current) 0 else glass.capacity

                val input = inputs.toList()[glassIndex]
                (input as? HTMLInputElement)?.apply {
                    value = newValue.toString()
                    dispatchEvent(Event("change"))

                    val action = mockStore.shouldHaveAction<UiState, SetGlassCurrent>()
                    action.shouldMatch {
                        val (isInitial, index, quantity) = it.payload
                        isInitial && (index == glassIndex) && (quantity == newValue)
                    }
                }
                done()
            }
            .catch { done(it) }
    }

    it.skip.async("5. should update initial capacity") { done ->

        done()
        val mockStore = MockStore(uiState, createReducer())
        val glassIndex = 0
        selectAllInMain(store = mockStore, selector = "#init-capacity-$glassIndex")
            .then { inputs ->

                val glass = uiState.finalState[glassIndex]
                val newValue = if (glass.capacity == config.maxCapacity) config.minCapacity else config.maxCapacity

                val input = inputs.toList()[glassIndex]
                (input as? HTMLInputElement)?.apply {
                    value = newValue.toString()
                    dispatchEvent(Event("change"))

                    val action = mockStore.shouldHaveAction<UiState, SetGlassCapacity>()
                    action.shouldMatch {
                        val (index, quantity) = it.payload
                        (index == glassIndex) && (quantity == newValue)
                    }
                }
                done()
            }
            .catch { done(it) }

    }

}

