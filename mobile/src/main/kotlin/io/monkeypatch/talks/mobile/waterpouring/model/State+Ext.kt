package io.monkeypatch.talks.mobile.waterpouring.model

import io.monkeypatch.talks.waterpouring.model.*


// State extension
/**
 * Apply a [Move] to a [State]
 *
 * @param move the [Move] to apply
 * @return the new [State]
 */
fun State.move(move: Move): State =
        this.mapIndexed { index, glass ->
            when (move) {
                is Empty -> if (index == move.index) glass.empty() else glass
                is Fill -> if (index == move.index) glass.fill() else glass
                is Pour ->
                    when (index) {
                        move.from -> glass - this[move.to].remainingVolume()
                        move.to -> glass + this[move.from].current
                        else -> glass
                    }
            }
        }

/**
 * Converts a List of Move to a List<Pair<Move?, State> using the initial State
 */
fun List<Move>.toSolutionList(initialState: State): List<Pair<Move?, State>> {
    val seed: List<Pair<Move?, State>> = listOf(null to initialState)
    return this.fold(seed) { list, move ->
        val (_, state) = list.last()
        list + (move to state.move(move))
    }
}
