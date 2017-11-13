@file:Suppress("ClassName")

package io.monkeypatch.talks.waterpouring.server

import io.monkeypatch.talks.waterpouring.model.*
import org.amshove.kluent.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class SolverTests {

    @Nested
    @DisplayName("2.1/ TailRecursiveSolver.findSolution()")
    class TailRecursiveSolver_findSolution {
        private val solver = TailRecursiveSolver()

        @Test
        fun `return null for an empty history`() {
            val expectedState = "0/2, 0/1".toState()
            val statesWithHistory = emptyList<StateWithHistory>()
            val moves = solver.findSolution(statesWithHistory, expectedState)

            moves.shouldBeNull()
        }

        @Test
        fun `return null for an history without the state`() {
            val expectedState = "0/2, 0/1".toState()
            val statesWithHistory = listOf(
                    "0/3, 0/1".toState() to listOf(Empty(1)),
                    "0/4, 0/1".toState() to listOf(Empty(1)),
                    "0/5, 0/1".toState() to listOf(Empty(1)))
            val moves = solver.findSolution(statesWithHistory, expectedState)

            moves.shouldBeNull()
        }

        @Test
        fun `return list of move for an history with the state`() {
            val expectedState = "0/2, 0/1".toState()
            val expectedSolution = listOf(Fill(1))
            val statesWithHistory = listOf(
                    "0/3, 0/1".toState() to listOf(Empty(1)),
                    "0/4, 0/1".toState() to listOf(Empty(1)),
                    "0/5, 0/1".toState() to listOf(Empty(1)),
                    expectedState to expectedSolution)
            val moves = solver.findSolution(statesWithHistory, expectedState)

            moves shouldEqual expectedSolution
        }
    }

    @Nested
    @DisplayName("2.2/ State.availableMoves()")
    class State_availableMoves {

        @Test
        fun `return an empty collection if state contains no Glass`() {
            val state = emptyList<Glass>()
            val moves = state.availableMoves()

            moves.shouldBeEmpty()
        }

        @Test
        fun `return an Empty if state contains one filled Glass`() {
            val state = "10/10".toState()
            val moves = state.availableMoves()

            moves.size shouldBe 1
            moves shouldContain Empty(0)
        }

        @Test
        fun `return an Fill if state contains one filled Glass`() {
            val state = "0/10".toState()
            val moves = state.availableMoves()

            moves.size shouldBe 1
            moves shouldContain Fill(0)
        }

        @Test
        fun `return an Fill and an Empty if state contains one Glass neither empty nor fill`() {
            val state = "4/10".toState()
            val moves = state.availableMoves()

            moves.size shouldBe 2
            moves shouldContainAll listOf(Empty(0), Fill(0))
        }

        @Test
        fun `return six moves for 2 Glasses neither empty nor fill`() {
            val state = "5/10, 1/5".toState()
            val moves = state.availableMoves()

            moves.size shouldBe 6
            moves shouldContainAll listOf(Empty(0), Fill(0),
                                          Empty(1), Fill(1),
                                          Pour(0, 1), Pour(1, 0))
        }
    }


    @Nested
    @DisplayName("2.3/ State.process()")
    class State_process {
        private val initialState = "4/5, 1/3, 0/2".toState()

        @Test
        fun `should process Empty`() {
            val state = initialState.process(Empty(0))
            val expected = "0/5, 1/3, 0/2".toState()

            state.forEachIndexed { index, glass ->
                expected[index] shouldEqual glass
            }
        }

        @Test
        fun `should process Fill`() {
            val state = initialState.process(Fill(1))
            val expected = "4/5, 3/3, 0/2".toState()

            state.forEachIndexed { index, glass ->
                expected[index] shouldEqual glass
            }
        }

        @Test
        fun `should process Pour`() {
            val state = initialState.process(Pour(from = 0, to = 2))
            val expected = "2/5, 1/3, 2/2".toState()

            state.forEachIndexed { index, glass ->
                expected[index] shouldEqual glass
            }
        }
    }

    @Nested
    @DisplayName("2.4/ TailRecursiveSolver.nextStatesFromState()")
    class TailRecursiveSolver_nextStatesFromState {
        private val solver = TailRecursiveSolver()

        @Test
        fun `return list of new state with history`() {
            val state = "5/10, 1/5".toState()
            val next = solver.nextStatesFromState(state to emptyList())

            next shouldContainAll listOf(
                    "0/10, 1/5".toState() to listOf(Empty(0)),
                    "10/10, 1/5".toState() to listOf(Fill(0)),
                    "5/10, 0/5".toState() to listOf(Empty(1)),
                    "5/10, 5/5".toState() to listOf(Fill(1)),
                    "1/10, 5/5".toState() to listOf(Pour(0, 1)),
                    "6/10, 0/5".toState() to listOf(Pour(1, 0)))
        }

    }

    @Nested
    @DisplayName("2.5/ TailRecursiveSolver.nextStatesFromCollection()")
    class TailRecursiveSolver_nextStatesFromCollection {
        private val solver = TailRecursiveSolver()

        @Test
        fun `return list of new state with history`() {
            val states = listOf<StateWithHistory>(
                    "10/10".toState() to emptyList(),
                    "0/10".toState() to emptyList())
            val next = solver.nextStatesFromCollection(states)

            next.size shouldBe 2
            next shouldContainAll listOf(
                    "0/10".toState() to listOf(Empty(0)),
                    "10/10".toState() to listOf(Fill(0)))
        }

    }

    @Nested
    @DisplayName("2.6/ TailRecursiveSolver.allVisitedStates()")
    class TailRecursiveSolver_allVisitedStates {
        private val solver = TailRecursiveSolver()

        @Test
        fun `return list of new state with history`() {
            val visitedStates = setOf(
                    "2/10".toState(),
                    "5/10".toState(),
                    "10/10".toState())

            val newStates = listOf(
                    "0/10".toState() to listOf(Empty(0)),
                    "10/10".toState() to listOf(Fill(0)))

            val states = solver.allVisitedStates(visitedStates, newStates)

            states.size shouldBe 4
            states shouldContainAll setOf(
                    "0/10".toState(),
                    "2/10".toState(),
                    "5/10".toState(),
                    "10/10".toState())
        }
    }

    @Nested
    @DisplayName("2.7/ TailRecursiveSolver.solve()")
    class TailRecursiveSolver_solve {
        private val solver = TailRecursiveSolver()

        @Test
        fun `should solve 0|5 0|3 to 4|5 0|3`() {
            val from = listOf(Glass(5), Glass(3))
            val to = listOf(Glass(5, 4), Glass(3))

            val solution = solver.solve(from, to)

            solution.size shouldBe 7
        }

        @Test
        fun `should solve 0|8 0|5 to 6|8 0|5`() {
            val from = listOf(Glass(8), Glass(5))
            val to = listOf(Glass(8, 6), Glass(5))

            val solution = solver.solve(from, to)

            solution.size shouldBe 7
        }

        @Test
        fun `should solve 0|24 0|13 0|11 0|5 to 6|24 6|13 6|11 0|5`() {
            val from = "0/24, 0/13, 0/11, 0/5".toState()
            val to = "6/24, 6/13, 6/11, 0/5".toState()

            val solution = solver.solve(from, to)

            solution.size shouldBe 10
        }


        @Test()
        fun `should fail if there is no solution`() {
            val from = "0/8, 0/4, 0/2".toState()
            listOf(Glass(8), Glass(4), Glass(2))
            val to = "1/8, 0/4, 0/2".toState()

            Assertions.assertThrows(IllegalStateException::class.java) {
                solver.solve(from, to)
            }
        }
    }

}
