package redux

import io.monkeypatch.talks.waterpouring.model.Glass
import model.UiState
import store.Action

/**
 * The application reducer
 */
fun createReducer() = { state: UiState, action: Action<*> ->
    when (action) {
        is SolveActionSuccess ->
            // set solution, remove error
            state.copy(solution = action.payload,
                       error = null)
        is SolveActionError   ->
            // update state error
            state.copy(error = action.payload.toString())
        is AddGlassAction     ->
            // add a glass to initial and final state,
            // empty error and solution
            state.copy(
                initialState = state.initialState + action.payload,
                finalState = state.finalState + action.payload.copy(current = 0),
                error = null,
                solution = emptyList())
        is RemoveGlassAction  ->
            // remove a glass to initial and final state,
            // empty error and solution
            state.copy(
                initialState = state.initialState.dropLast(1),
                finalState = state.finalState.dropLast(1),
                error = null,
                solution = emptyList())
        is SetGlassCapacity   -> {
            // update glass capacity value based on same index into initial an final state,
            // empty error and solution,
            // warn the current value never should be > capacity,
            val (idx, newCapacity) = action.payload
            val setCapacity: (Int, Glass) -> Glass = { index, glass ->
                if (index == idx)
                    glass.copy(capacity = newCapacity,
                               current = glass.current.coerceAtMost(newCapacity))
                else glass
            }
            state.copy(initialState = state.initialState.mapIndexed(setCapacity),
                       finalState = state.finalState.mapIndexed(setCapacity),
                       error = null,
                       solution = emptyList())
        }
        is SetGlassCurrent    -> {
            // update glass current value based on same index into initial an final state
            // empty error and solution,
            // warn the capacity value should be >= current
            val (isInitial, idx, newCurrent) = action.payload
            val setCurrent: (Int, Glass) -> Glass = { index, glass ->
                if (index == idx)
                    glass.copy(current = newCurrent,
                               capacity = glass.capacity.coerceAtLeast(newCurrent))
                else glass
            }
            if (isInitial)
                state.copy(initialState = state.initialState.mapIndexed(setCurrent),
                           error = null,
                           solution = emptyList())
            else
                state.copy(finalState = state.finalState.mapIndexed(setCurrent),
                           error = null,
                           solution = emptyList())
        }

        else                  ->
            // by default return the state
            state
    }
}