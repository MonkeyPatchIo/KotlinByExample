package io.monkeypatch.talks.mobile.waterpouring

import com.fasterxml.jackson.module.kotlin.readValue
import io.monkeypatch.talks.mobile.waterpouring.api.SolverApi
import io.monkeypatch.talks.mobile.waterpouring.model.*
import io.monkeypatch.talks.mobile.waterpouring.test.shouldBe
import io.monkeypatch.talks.waterpouring.model.*
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Rule
import org.junit.Test

@Suppress("FunctionName")
open class SolveTest {

    @get:Rule
    private val server = MockWebServer()

    private val initialState = "4/5, 1/4, 0/2".toState()
    private val moves = listOf(
            Empty(index = 1),
            Pour(from = 0, to = 2),
            Fill(index = 0))

    @Test
    fun _2_1_Solve_call_server() {
        val url = server.url("/").toString()
        Configuration.url = url
        val input = initialState to initialState

        val json = waterPouringMapper.writeValueAsString(moves)
        server.enqueue(MockResponse().setBody(json))

        val result = SolverApi.solve(input.first, input.second)
                .blockingGet()

        result shouldBe moves

        val request = server.takeRequest()
        val bodySent: Pair<State, State> = waterPouringMapper.readValue(request.body.readUtf8())

        request.requestLine shouldBe "POST /api/solve HTTP/1.1"
        bodySent shouldBe input
    }


    @Test
    fun _2_2_Solve_State_move_Empty() {
        val (glass0, glass1, glass2) = initialState
        val expectedEmpty = listOf(glass0.empty(), glass1, glass2)

        val stateEmpty = initialState.move(Empty(0))

        stateEmpty.forEachIndexed { index, glass ->
            expectedEmpty[index] shouldBe glass
        }
    }

    @Test
    fun _2_2_Solve_State_move_Fill() {
        val (glass0, glass1, glass2) = initialState
        val expectedFill = listOf(glass0, glass1.fill(), glass2)

        val stateFill = initialState.move(Fill(1))

        stateFill.forEachIndexed { index, glass ->
            expectedFill[index] shouldBe glass
        }
    }

    @Test
    fun _2_2_Solve_State_move_Pour() {
        val (glass0, glass1, glass2) = initialState
        val expectedPour = listOf(glass0.minus(glass2.remainingVolume()),
                                  glass1,
                                  glass2.plus(glass0.current))

        val statePour = initialState.move(Pour(from = 0, to = 2))

        statePour.forEachIndexed { index, glass ->
            expectedPour[index] shouldBe glass
        }
    }

    @Test
    fun _2_3_Solve_State_solution_List() {
        val (move0, move1, move2) = moves

        val expected: List<Pair<Move?, State>> = listOf(
                null to initialState,
                move0 to initialState.move(move0),
                move1 to initialState.move(move0).move(move1),
                move2 to initialState.move(move0).move(move1).move(move2))

        val solutionList = moves.toSolutionList(initialState)

        solutionList.forEachIndexed { index, (move, state) ->
            val (expectedMove, expectedState) = expected[index]
            move shouldBe expectedMove
            state shouldBe expectedState
        }
    }


}