package io.monkeypatch.talks.waterpouring.web

import io.monkeypatch.talks.waterpouring.model.Empty
import io.monkeypatch.talks.waterpouring.model.Fill
import io.monkeypatch.talks.waterpouring.model.Move
import io.monkeypatch.talks.waterpouring.model.Pour
import io.monkeypatch.talks.waterpouring.model.State
import io.monkeypatch.talks.waterpouring.model.toState
import io.monkeypatch.talks.waterpouring.web.test.MockStore
import io.monkeypatch.talks.waterpouring.web.test.asIterable
import io.monkeypatch.talks.waterpouring.web.test.selectInMain
import io.monkeypatch.talks.waterpouring.web.test.shouldBe
import io.monkeypatch.talks.waterpouring.web.test.shouldBeSolutionElement
import io.monkeypatch.talks.waterpouring.web.test.shouldHaveAction
import io.monkeypatch.talks.waterpouring.web.test.shouldHavePayload
import io.monkeypatch.talks.waterpouring.web.test.shouldHaveSize
import io.monkeypatch.talks.waterpouring.web.test.uiState
import mocha.async
import mocha.describe
import mocha.invoke
import mocha.it
import model.UiState
import model.empty
import model.fill
import model.minus
import model.move
import model.plus
import model.remainingVolume
import org.w3c.dom.HTMLButtonElement
import redux.SolveAction
import redux.SolveActionSuccess
import redux.SolveMove
import redux.asMove
import redux.createReducer
import redux.solveEffect
import kotlin.js.Promise
import kotlin.test.fail


@Suppress("UnsafeCastFromDynamic")
val testSolve = describe("[4] Solve") {

    val initialState = "4/5, 1/4, 0/2".toState()
    val finalState = "4/5, 1/4, 0/2".toState()

    val emptySolveMove = object : SolveMove {
        override val type: String
            get() = "Empty"
        override val index: Int?
            get() = 1
        override val from: Int?
            get() = null
        override val to: Int?
            get() = null
    }

    val fillSolveMove = object : SolveMove {
        override val type: String
            get() = "Fill"
        override val index: Int?
            get() = 1
        override val from: Int?
            get() = null
        override val to: Int?
            get() = null
    }

    val pourSolveMove = object : SolveMove {
        override val type: String
            get() = "Pour"
        override val index: Int?
            get() = null
        override val from: Int?
            get() = 1
        override val to: Int?
            get() = 3
    }

    val moves: List<Move> = listOf(Empty(1), Pour(from = 0, to = 2), Fill(0))

    it.async("1. should trigger solve action") { done ->
        val mockStore = MockStore(uiState, createReducer())

        selectInMain<Any>(store = mockStore, selector = ".btn-solve")
            .then { button ->
                (button as? HTMLButtonElement)?.click()
                mockStore.shouldHaveAction<UiState, SolveAction>()
                done()
            }
            .catch { done(it) }
    }

    describe("2. SolveMove to Move") {
        it("should convert empty SolveMove to Move") {
            emptySolveMove.asMove() shouldBe Empty(1)
        }
        it("should convert fill SolveMove to Move") {
            fillSolveMove.asMove() shouldBe Fill(1)
        }
        it("should convert pour SolveMove to Move") {
            pourSolveMove.asMove() shouldBe Pour(1, 3)
        }
    }

    it.async("3. should send post when solve") { done ->
        var url: String? = null
        var body: Pair<State, State>? = null
        val backend = "plop"
        val uiState = uiState.copy(initialState = initialState, finalState = finalState)
        val input = uiState.initialState to uiState.finalState
        val effect = solveEffect(backend) { solverUrl, solverInput ->
            url = solverUrl
            body = solverInput
            Promise.resolve(arrayOf(emptySolveMove, fillSolveMove, pourSolveMove))
        }

        val result = effect(SolveAction(input))
        url shouldBe backend
        body shouldBe input

        result
            .then { action ->
                when (action) {
                    is SolveActionSuccess ->
                        action shouldHavePayload { moves ->
                            moves[0] shouldBe Empty(1)
                            moves[1] shouldBe Fill(1)
                            moves[2] shouldBe Pour(1, 3)
                            true
                        }
                    else                  -> fail("should be a SolveActionSuccess")
                }
            }
            .then { done() }
            .catch { done(it) }
    }

    describe("4. State#move") {
        it("should apply an Empty move") {
            val (glass0, glass1, glass2) = initialState
            val expectedEmpty = listOf(glass0.empty(), glass1, glass2)

            val stateEmpty = initialState.move(Empty(0))

            stateEmpty.forEachIndexed { index, glass ->
                expectedEmpty[index] shouldBe glass
            }
        }

        it("should apply an Fill move") {
            val (glass0, glass1, glass2) = initialState
            val expectedFill = listOf(glass0, glass1.fill(), glass2)

            val stateFill = initialState.move(Fill(1))

            stateFill.forEachIndexed { index, glass ->
                expectedFill[index] shouldBe glass
            }
        }

        it("should apply an Pour move") {
            val (glass0, glass1, glass2) = initialState
            val expectedPour = listOf(glass0.minus(glass2.remainingVolume()),
                                      glass1,
                                      glass2.plus(glass0.current))

            val statePour = initialState.move(Pour(from = 0, to = 2))

            statePour.forEachIndexed { index, glass ->
                expectedPour[index] shouldBe glass
            }
        }
    }


    describe("5. UiState#solutionList") {
        it("should convert moves") {
            val uiState = UiState(initialState = initialState, finalState = initialState, solution = moves)
            val (move0, move1, move2) = moves
            val expected: List<Pair<Move?, State>> = listOf(
                null to initialState,
                move0 to initialState.move(move0),
                move1 to initialState.move(move0).move(move1),
                move2 to initialState.move(move0).move(move1).move(move2))

            val solutionList = uiState.solutionList

            solutionList.forEachIndexed { index, (move, state) ->
                val (expectedMove, expectedState) = expected[index]
                move shouldBe expectedMove
                state shouldBe expectedState
            }
        }
    }

    it.skip.async("6. should render solution State") { done ->
        val uiState = UiState(initialState = initialState, finalState = initialState, solution = moves)
        val mockStore = MockStore(uiState, createReducer())
        selectInMain<Any>(store = mockStore, selector = "ul")
            .then { list ->
                val nodes = list.childNodes.asIterable().toList()
                nodes shouldHaveSize uiState.solutionList.size

                nodes.zip(uiState.solutionList)
                    .forEach { (node, pair) ->
                        node shouldBeSolutionElement pair
                    }

                done()
            }
            .catch { done(it) }
    }
}