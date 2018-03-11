package io.monkeypatch.talks.waterpouring.server

import io.kotlintest.matchers.collections.shouldContainAll
import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.shouldBe
import io.kotlintest.specs.ShouldSpec
import io.monkeypatch.talks.waterpouring.model.Empty
import io.monkeypatch.talks.waterpouring.model.Fill
import io.monkeypatch.talks.waterpouring.model.Glass
import io.monkeypatch.talks.waterpouring.model.Pour
import io.monkeypatch.talks.waterpouring.model.StateWithHistory
import io.monkeypatch.talks.waterpouring.model.availableMoves
import io.monkeypatch.talks.waterpouring.model.process
import io.monkeypatch.talks.waterpouring.model.toState
import io.monkeypatch.talks.waterpouring.server.service.TailRecursiveSolver
import kotlin.test.assertFailsWith

class SolverSpec : ShouldSpec() {

    init {

        "2.1/ TailRecursiveSolver.findSolution()" {
            val solver = TailRecursiveSolver()

            should("return null for an empty history") {
                val expectedState = "0/2, 0/1".toState()
                val statesWithHistory = emptyList<StateWithHistory>()

                val moves = solver.findSolution(statesWithHistory, expectedState)

                moves shouldBe null
            }

            should("return null for an history without the state") {
                val expectedState = "0/2, 0/1".toState()
                val statesWithHistory = listOf(
                    "0/3, 0/1".toState() to listOf(Empty(1)),
                    "0/4, 0/1".toState() to listOf(Empty(1)),
                    "0/5, 0/1".toState() to listOf(Empty(1)))

                val moves = solver.findSolution(statesWithHistory, expectedState)

                moves shouldBe null
            }

            should("return list of move for an history with the state") {
                val expectedState = "0/2, 0/1".toState()
                val expectedSolution = listOf(Fill(1))
                val statesWithHistory = listOf(
                    "0/3, 0/1".toState() to listOf(Empty(1)),
                    "0/4, 0/1".toState() to listOf(Empty(1)),
                    "0/5, 0/1".toState() to listOf(Empty(1)),
                    expectedState to expectedSolution)

                val moves = solver.findSolution(statesWithHistory, expectedState)

                moves shouldBe expectedSolution
            }
        }

        "2.2/ State.availableMoves()" {

            should("return an empty collection if state contains no Glass") {
                val state = emptyList<Glass>()

                val moves = state.availableMoves()

                moves shouldBe emptyList()
            }

            should("return an Empty if state contains one filled Glass") {
                val state = "10/10".toState()

                val moves = state.availableMoves()

                moves.shouldHaveSize(1)
                moves.shouldContainAll(Empty(0))
            }

            should("return an Fill if state contains one filled Glass") {
                val state = "0/10".toState()

                val moves = state.availableMoves()

                moves.shouldHaveSize(1)
                moves.shouldContainAll(Fill(0))
            }

            should("return an Fill and an Empty if state contains one Glass neither empty nor fill") {
                val state = "4/10".toState()

                val moves = state.availableMoves()

                moves.shouldHaveSize(2)
                moves.shouldContainAll(Empty(0), Fill(0))
            }

            should("return six moves for 2 Glasses neither empty nor fill") {
                val state = "5/10, 1/5".toState()

                val moves = state.availableMoves()

                moves.shouldHaveSize(6)
                moves.shouldContainAll(listOf(Empty(0), Fill(0),
                                              Empty(1), Fill(1),
                                              Pour(0, 1), Pour(1, 0)))
            }
        }

        "2.3/ State.process()" {
            val initialState = "4/5, 1/3, 0/2".toState()

            should("should process Empty") {

                val state = initialState.process(Empty(0))

                val expected = "0/5, 1/3, 0/2".toState()
                state shouldBe expected
            }

            should("should process Fill") {

                val state = initialState.process(Fill(1))

                val expected = "4/5, 3/3, 0/2".toState()
                state shouldBe expected
            }

            should("should process Pour") {

                val state = initialState.process(Pour(from = 0, to = 2))

                val expected = "2/5, 1/3, 2/2".toState()
                state shouldBe expected
            }
        }

        "2.4/ TailRecursiveSolver.nextStatesFromState()" {
            val solver = TailRecursiveSolver()

            should("return list of new state with history") {
                val state = "5/10, 1/5".toState()

                val next = solver.nextStatesFromState(state to emptyList())

                next.shouldContainAll(
                    "0/10, 1/5".toState() to listOf(Empty(0)),
                    "10/10, 1/5".toState() to listOf(Fill(0)),
                    "5/10, 0/5".toState() to listOf(Empty(1)),
                    "5/10, 5/5".toState() to listOf(Fill(1)),
                    "1/10, 5/5".toState() to listOf(Pour(0, 1)),
                    "6/10, 0/5".toState() to listOf(Pour(1, 0)))
            }

        }

        "2.5/ TailRecursiveSolver.nextStatesFromCollection()" {
            val solver = TailRecursiveSolver()

            should("return list of new state with history") {
                val states = listOf<StateWithHistory>(
                    "10/10".toState() to emptyList(),
                    "0/10".toState() to emptyList())

                val next = solver.nextStatesFromCollection(states)

                next.shouldHaveSize(2)
                next.shouldContainAll(
                    "0/10".toState() to listOf(Empty(0)),
                    "10/10".toState() to listOf(Fill(0)))
            }

        }

        "2.6/ TailRecursiveSolver.allVisitedStates()" {
            val solver = TailRecursiveSolver()

            should("return list of new state with history") {
                val visitedStates = setOf(
                    "2/10".toState(),
                    "5/10".toState(),
                    "10/10".toState())
                val newStates = listOf(
                    "0/10".toState() to listOf(Empty(0)),
                    "10/10".toState() to listOf(Fill(0)))

                val states = solver.allVisitedStates(visitedStates, newStates)

                states.shouldHaveSize(4)
                states.shouldContainAll(
                    "0/10".toState(),
                    "2/10".toState(),
                    "5/10".toState(),
                    "10/10".toState())
            }
        }

        "2.7/ TailRecursiveSolver.solve()" {
            val solver = TailRecursiveSolver()

            should("should solve 0/5, 0/3 to 4/5, 0/3") {
                val from = "0/5, 0/3".toState()
                val to = "4/5, 0/3".toState()

                val solution = solver.solve(from, to)

                solution.shouldHaveSize(7)
            }

            should("should solve 0/8, 0/5 to 6/8, 0/5") {
                val from = "0/8, 0/5".toState()
                val to = "6/8, 0/5".toState()

                val solution = solver.solve(from, to)

                solution.shouldHaveSize(7)
            }

            should("should solve 0/24, 0/13, 0/11, 0/5 to 6/24, 6/13, 6/11, 0/5") {
                val from = "0/24, 0/13, 0/11, 0/5".toState()
                val to = "6/24, 6/13, 6/11, 0/5".toState()

                val solution = solver.solve(from, to)

                solution.shouldHaveSize(10)
            }

            should("should fail if there is no solution") {
                val from = "0/8, 0/4, 0/2".toState()
                val to = "1/8, 0/4, 0/2".toState()

                assertFailsWith<IllegalStateException> {
                    solver.solve(from, to)
                }
            }

        }
    }
}