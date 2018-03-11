package store

import kotlin.js.Console
import kotlin.js.Promise


/**
 * In this file, <S> represent the application state
 *
 * It should be immutable
 */

/**
 * Define an action
 *
 * An action can trigger an change in stage (done in the reducer),
 * or trigger some effects
 */
interface Action<out P> {
    val payload: P
}

/**
 * Define the reducer
 *
 * A reducer should update the state (state should be immutable) based on actions
 */
typealias Reducer<S> = (S, Action<*>) -> S

/**
 * Define an effect
 *
 * An effect could generate an Action (asynchronous),
 * application side effect should be done in an effect,
 * the typical usage: call a backend REST api
 */
typealias Effect = (Action<*>) -> Promise<Action<*>?>

/**
 * Define a selector
 *
 * A selector is just a projection of the state
 */
typealias Selector<S, V> = (S) -> V

/**
 * Define a watcher
 *
 * It is triggered by the store when the state projection change
 */
typealias Watcher<V> = (V) -> Unit


/**
 * Define the store
 */
interface Store<out S> {

    interface Unsubscribe {
        fun invoke()
    }

    /**
     * Return a projection fo the current state
     *
     * @param projection the projection
     * @return the projected value
     */
    fun <V> select(projection: Selector<S, V>): V

    /**
     * Return the current state
     */
    val state: S
        get() = select { it }

    /**
     * Handle an action
     *
     * Might update the current state, based on the reducer,
     * and might trigger an Effect
     * @param action the action to handle
     */
    fun dispatch(action: Action<*>)

    /**
     * Register a watcher
     *
     * @param projection the projection
     * @param watcher the watcher
     */
    fun <V> subscribe(projection: Selector<S, V>, watcher: Watcher<V>): Unsubscribe

    companion object {
        /**
         * Build a Store
         *
         * @param initialState the application initial state
         * @param reducer the application reducer
         * @param effect all application [Effect] as `vararg`
         */
        fun <S> create(initialState: S,
                       reducer: Reducer<S>,
                       vararg effect: Effect): Store<S> =
            StoreImpl(console, initialState, reducer, effect.toList())
    }
}

/**
 * Default implementation for the store
 *
 * The store contains the state,
 * it provide a way to query state with a Selector,
 * or subscribe state changes with a selector and a Watcher.
 *
 * To update the state, send an Action that trigger a Reducer, or an Effect
 *
 * @property logger a logger
 * @param initialState the application initial state
 * @property reducer the application reducer
 * @property effects all application [Effect]
 */
private class StoreImpl<out S>(private val logger: Console,
                               initialState: S,
                               private val reducer: Reducer<S>,
                               private val effects: Collection<Effect> = emptyList()) : Store<S> {

    /** All watchers group by corresponding [Selector] */
    private val watchers = (mutableMapOf<Selector<S, *>, MutableList<Watcher<*>>>())

    /** The current state */
    private var _state: S = initialState

    /** Get all [Watcher] corresponding to a [Selector] */
    private fun <V> getWatchers(projection: Selector<S, V>) =
        watchers.getOrPut(projection) { mutableListOf() }

    override fun <V> select(projection: Selector<S, V>) = projection(_state)

    override fun <V> subscribe(projection: Selector<S, V>, watcher: Watcher<V>): Store.Unsubscribe {
        getWatchers(projection)
            .add(watcher)

        // initial subscribe
        watcher(select(projection))

        return object : Store.Unsubscribe {
            override fun invoke() {
                getWatchers(projection)
                    .remove(watcher)
            }
        }
    }

    override fun dispatch(action: Action<*>) {
        logger.info("action", action)
        // Apply reducer
        val selectValues = projections()
        _state = reducer(_state, action)
        val updatedValues = projections()

        // Notify watchers if necessary
        updatedValues.forEach { (projection, newValue) ->
            if (selectValues[projection] != newValue) {
                getWatchers(projection)
                    .filterIsInstance<Watcher<Any?>>()
                    .forEach { watcher -> watcher(newValue) }
            }
        }

        // Trigger Effects
        effects.forEach { effect ->
            effect(action)
                .then({ newAction -> if (newAction != null) dispatch(newAction) },
                      { error -> logger.error(error) })
        }
    }

    /** Get projected values from the current state */
    private fun projections(): Map<Selector<S, *>, Any?> =
        watchers.keys
            .map { it to select(it) }
            .toMap()
}