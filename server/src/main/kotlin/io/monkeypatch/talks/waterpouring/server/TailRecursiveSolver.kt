package io.monkeypatch.talks.waterpouring.server

import io.monkeypatch.talks.waterpouring.model.*
import org.springframework.stereotype.Service


/**
 * An implementation of [Solver] using a tail-recursive auxiliary function
 *
 * The `@Service` annotation allow dependency injection with this class
 */
@Service
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
        val (state, moves) = stateWithHistory
        return state.availableMoves()
            .map { move -> (state.process(move)) to (moves + move) }
    }

    /**
     * Determine next [State]s
     *
     * @param statesWithHistory last [StateWithHistory] list
     * @return all next [StateWithHistory]
     */
    internal fun nextStatesFromCollection(statesWithHistory: Collection<StateWithHistory>): List<StateWithHistory> =
        statesWithHistory.flatMap(this::nextStatesFromState)

    /**
     * Compute all visited [State] with previous visited [State], and new ones
     *
     * @param visitedStates already visited [State]
     * @param newlyStates new [State]s
     * @return set of all visited [State]s, including the new ones
     */
    internal fun allVisitedStates(visitedStates: Set<State>, newlyStates: List<StateWithHistory>): Set<State> =
        visitedStates + newlyStates.map { it.first }

    /**
     * Solve Water Pouring Puzzle
     *
     * @param from the initial [State]
     * @param to the expected [State]
     * @return list of [Move]s required to solve the puzzle
     * @throws IllegalStateException if no solution found
     */
    override fun solve(from: State, to: State): List<Move> {
        // Auxiliary function
        tailrec fun solveAux(statesWithHistory: Collection<StateWithHistory>, visitedStates: Set<State>): List<Move> {
            // Check end of recursion
            val found = findSolution(statesWithHistory, to)
            if (found != null) {
                return found
            }

            // Compute next states
            val next = nextStatesFromCollection(statesWithHistory)
                .filterNot { (state, _) -> visitedStates.contains(state) } // keep only new states

            // Compute new visited state
            val nextVisited = allVisitedStates(visitedStates, next)

            // Avoid infinite loop
            if (statesWithHistory == next) {
                val states = visitedStates
                    .joinToString(separator = "\n", limit = 10) { it.toDisplayString() }
                throw IllegalStateException("Infinite loop: No solution found\nLast states: $states")
            }

            // Tail recursion
            return solveAux(next, nextVisited)
        }

        return solveAux(listOf(from to emptyList()), setOf(from))
    }


}