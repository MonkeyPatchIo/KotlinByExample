package io.monkeypatch.talks.waterpouring.server.service

import io.monkeypatch.talks.waterpouring.model.Move
import io.monkeypatch.talks.waterpouring.model.State

/**
 * Define a Solver for [Water Pouring Puzzle](https://en.wikipedia.org/wiki/Water_pouring_puzzle)
 *
 * Using an interface allow to have different implementations, like a mock one for testing
 */
interface Solver {
    /**
     * Solve a Water Pouring Puzzle
     *
     * @param from initial [State]
     * @param to expected [State]
     * @return list of [Move]s required
     * @throws IllegalStateException if no solution found
     */
    fun solve(from: State, to: State): List<Move>

}
