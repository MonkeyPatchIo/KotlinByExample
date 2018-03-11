package io.monkeypatch.talks.waterpouring.web.test

import store.Action
import store.Reducer
import store.Selector
import store.Store
import store.Watcher

/**
 * A Mock implementation for [Store]
 */
class MockStore<out S>(initialState: S, private val reducer: Reducer<S>) : Store<S> {

    private var _state: S = initialState
    val actions = mutableListOf<Action<*>>()
    private val watchers = (mutableMapOf<Selector<S, *>, MutableList<Watcher<*>>>())

    override fun <V> select(projection: Selector<S, V>): V =
        projection(_state)

    override fun dispatch(action: Action<*>) {
        actions.add(action)
        // update state
        _state = reducer(_state, action)
    }

    private fun <V> getWatchers(projection: Selector<S, V>) =
        watchers.getOrPut(projection) { mutableListOf() }


    override fun <V> subscribe(projection: Selector<S, V>, watcher: Watcher<V>): Store.Unsubscribe {
        val list = getWatchers(projection)
        list.add(watcher)
        // initial subscribe
        watcher(select(projection))
        return object : Store.Unsubscribe {
            override fun invoke() {
                getWatchers(projection)
                    .remove(watcher)
            }
        }
    }

}