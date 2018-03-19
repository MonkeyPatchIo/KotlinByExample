package model

import io.monkeypatch.talks.waterpouring.model.Move
import io.monkeypatch.talks.waterpouring.model.State
import io.monkeypatch.talks.waterpouring.model.toState

/**
 * Main application state
 */
data class UiState(
        val initialState: State,
        val finalState: State,
        val error: String? = null,
        val solution: List<Move> = emptyList()) {

    constructor(config: Configuration) : this(
        initialState = config.initial.toState(),
        finalState = config.final.toState())

    /**
     * Build a solution as a [List] of [Pair] containing the [Move] and the resulting [State],
     * the first item of the [List] does not contains a [Move], just the initial [State]
     */
    val solutionList: List<Pair<Move?, State>> by lazy {
        val seed = listOf<Pair<Move?, State>>(null to initialState)
        solution.fold(seed) { acc, move ->
            val (_, state) = acc.last()
            acc + (move to (state.move(move)))
        }
    }

}
