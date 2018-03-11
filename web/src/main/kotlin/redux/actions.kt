package redux

import io.monkeypatch.talks.waterpouring.model.Glass
import io.monkeypatch.talks.waterpouring.model.Move
import io.monkeypatch.talks.waterpouring.model.State
import store.Action

/**
 * Add a [Glass] action
 *
 * @property payload the [Glass] to add
 */
class AddGlassAction(override val payload: Glass) : Action<Glass>

/**
 * Remove a [Glass] action
 */
object RemoveGlassAction : Action<Unit> {
    override val payload = Unit
}

/**
 * Set a [Glass] capacity action
 *
 * @property payload a [Pair] with index of the [Glass] and the new capacity
 */
class SetGlassCapacity(override val payload: Pair<Int, Int>) : Action<Pair<Int, Int>>

/**
 * Set a [Glass] current action
 *
 * @property payload a [Triple] with flag telling if the [Glass] is on the initial state (`true`) or final state (`false`),
 *                   an index of the [Glass],
 *                   and the new current
 */
class SetGlassCurrent(override val payload: Triple<Boolean, Int, Int>) : Action<Triple<Boolean, Int, Int>> {
    /**
     * Alternate constructor
     * @param isInitialState is the action concerning the initial state (`true`) or the final (`false`)
     * @param glassIndex the glass index
     * @param newCurrentValue the new value
     */
    constructor(isInitialState: Boolean, glassIndex: Int, newCurrentValue: Int) : this(Triple(isInitialState,
                                                                                              glassIndex,
                                                                                              newCurrentValue))
}

/**
 * Solve action
 *
 * @property payload a [Pair] containing initial [State] and final [State]
 */
class SolveAction(override val payload: Pair<State, State>) : Action<Pair<State, State>>

/**
 * A successful sloven return action
 *
 * @property payload a [List] of [Move] to solve the problem
 */
class SolveActionSuccess(override val payload: List<Move>) : Action<List<Move>>

/**
 * An erroneous unsolved return action
 *
 * @property payload the returned error
 */
class SolveActionError(override val payload: Throwable) : Action<Throwable>
