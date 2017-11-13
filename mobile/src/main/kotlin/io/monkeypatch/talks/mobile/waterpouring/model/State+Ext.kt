package io.monkeypatch.talks.mobile.waterpouring.model

import io.monkeypatch.talks.waterpouring.model.*


// State extension
/**
 * Apply a [Move] to a [State]
 *
 * @param move the [Move] to apply
 * @return the new [State]
 */
fun State.move(move: Move): State = TODO("2.2")

/**
 * Converts a List of Move to a List<Pair<Move?, State> using the initial State
 */
fun List<Move>.toSolutionList(initialState: State): List<Pair<Move?, State>> {
    TODO("2.3")
}
