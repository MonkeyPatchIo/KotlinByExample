package io.monkeypatch.talks.waterpouring.server.service


import io.monkeypatch.talks.waterpouring.model.*


/**
 * An implementation of [Solver] using a tail-recursive auxiliary function
 *
 */
class TailRecursiveSolver : Solver {

    /**
     * Try to found [State] matching the expected one, and return the history
     *
     * @param statesWithHistory list of state and history to check
     * @param expected the expected [State]
     * @return a list of [Move] or null if not found
     */
    internal fun findSolution(statesWithHistory: Collection<StateWithHistory>, expected: State): List<Move>? =
        statesWithHistory.find { (state, _) -> state == expected }
            ?.second

    /**
     * Computes next [State]s
     *
     * @param stateWithHistory a [StateWithHistory]
     * @return all next [StateWithHistory] with processing all available [Move] of [State]
     */
    internal fun nextStatesFromState(stateWithHistory: StateWithHistory): List<StateWithHistory> {
        val (state, history) = stateWithHistory
        return state.availableMoves()
            .map { move -> (state.process(move)) to (history + move) }
    }

    /**
     * Determine next [State]s
     *
     * @param statesWithHistory last [StateWithHistory] list
     * @return all next [StateWithHistory]
     */
    internal fun nextStatesFromCollection(statesWithHistory: Collection<StateWithHistory>): List<StateWithHistory> =
        statesWithHistory.flatMap { nextStatesFromState(it) } // 💖 flatMap

    /**
     * Compute all visited [State] with previous visited [State], and new ones
     *
     * @param visitedStates already visited [State]
     * @param newlyStates new [State]s
     * @return set of all visited [State]s, including the new ones
     */
    internal fun allVisitedStates(visitedStates: Set<State>, newlyStates: List<StateWithHistory>): Set<State> =
        TODO("2.6")

    /**
     * Solve Water Pouring Puzzle
     *
     * @param from the initial [State]
     * @param to the expected [State]
     * @return list of [Move]s required to solve the puzzle
     * @throws IllegalStateException if no solution found
     */
    override fun solve(from: State, to: State): List<Move> {
        TODO("2.7")
    }


}