package redux

import helpers.postJson
import io.monkeypatch.talks.waterpouring.model.*
import store.Action
import store.Effect
import kotlin.js.Promise

/**
 * The interface [SolveMove] to match the REST API response body result
 */
interface SolveMove {
    val type: String
    val index: Int?
    val from: Int?
    val to: Int?
}

/**
 * Transform a [SolveMove] to a [Move]
 *
 * @return the [Move] corresponding
 */
fun SolveMove.asMove(): Move =
        when(type) {
            "Empty" -> Empty(index?: throw IllegalArgumentException("Expected an index"))
            "Fill" -> Fill(index?: throw IllegalArgumentException("Expected an index"))
            "Pour" -> Pour (
                from = from?:throw IllegalArgumentException("Expected an from"),
                to = to?:throw IllegalArgumentException("Expected an to"))
               else -> throw IllegalArgumentException("Unsupported type: $type")
        }

/**
 * A solver should take an URL, and a [Pair] of [State] (first: initial, second: final),
 * and return a [Promise] of [Array] of [SolveMove]
 */
typealias Solver = (String, Pair<State, State>) -> Promise<Array<SolveMove>>

/**
 * [Effect] that call backend REST API to retrieve a solution
 * postJson(url: String, input: Pair<State, State>): Promise<Array<SolveMove>>
 */
fun solveEffect(solverUrl: String, solver: Solver): Effect = { action ->
    when (action) {
        is SolveAction -> TODO("4.3")
    // then transform to a List<Move>,
    // return the success or the failure action
        else           -> Promise.resolve(null as Action<*>?)
    }
}