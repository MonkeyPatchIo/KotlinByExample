package components

import helpers.random
import io.monkeypatch.talks.waterpouring.model.Glass
import kotlinx.html.js.onClickFunction
import model.Configuration
import model.UiState
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.ReactElement
import react.dom.button
import react.dom.div
import react.dom.h1
import react.dom.ul
import react.setState
import redux.AddGlassAction
import redux.RemoveGlassAction
import redux.SetGlassCapacity
import redux.SetGlassCurrent
import redux.SolveAction
import store.Store


/**
 * Create the main structure:
 *
 * ```html
 * <div class="main">
 *     <h1>Kotlin par l'exemple</h1>
 *     <button>Ajouter un verre</button>
 *     <button>Supprimer un verre</button>
 *     <div class="state initial"></div>
 *     <div class="state final"></div>
 *     <button class="btn-solve">Résoudre</button>
 *     <div class="error"></div>
 *     <ul></ul>
 * </div>
 * ```
 */

interface MainProps : RProps {
    var store: Store<UiState>
    var minCapacity: Int
    var maxCapacity: Int

    var onAddGlass: () -> Unit
    var onRemoveGlass: () -> Unit

    var onInitialCapacityChange: (Int, Int) -> Unit
    var onInitialCurrentChange: (Int, Int) -> Unit

    var onFinalCurrentChange: (Int, Int) -> Unit

    var onSolve: () -> Unit
}

interface MainState : RState {
    var uiState: UiState
}

class MainComponent(props: MainProps) : RComponent<MainProps, MainState>(props) {

    private var unsubscribe: Store.Unsubscribe? = null

    private val watcher = { newUiState: UiState ->
        setState {
            uiState = newUiState
        }
    }

    override fun MainState.init(props: MainProps) {
        uiState = props.store.state
    }

    override fun componentDidMount() {
        unsubscribe = props.store.subscribe({ it }, watcher)
    }

    override fun componentWillUnmount() {
        unsubscribe?.invoke()
    }

    override fun RBuilder.render() {
        div(classes = "main") {
            h1 { +"Kotlin par l'exemple" }
            button(classes = "btn-add") {
                +"Ajouter un verre"
                attrs.onClickFunction = { _ -> props.onAddGlass() }
            }
            button(classes = "btn-remove") {
                +"Supprimer un verre"
                attrs.onClickFunction = { _ -> props.onRemoveGlass() }
            }
            div(classes = "state initial") {
                initialState(state = state.uiState.initialState,
                             minCapacity = props.minCapacity,
                             maxCapacity = props.maxCapacity,
                             onInitialCapacityChange = props.onInitialCapacityChange,
                             onInitialCurrentChange = props.onInitialCurrentChange)
            }
            div(classes = "state final") {
                finalState(state = state.uiState.finalState,
                           onFinalCurrentChange = props.onFinalCurrentChange)
            }
            button(classes = "btn-solve") {
                +"Résoudre"
                attrs.onClickFunction = { _ -> props.onSolve() }
            }
            div(classes = "error") {
                +(state.uiState.error ?: "")
            }
            ul {
                if (state.uiState.solutionList.size > 1) {
                    solutionState(state.uiState.solutionList)
                }
            }
        }
    }
}

// Main container
fun RBuilder.mainContainer(config: Configuration, store: Store<UiState>): ReactElement =
    child(MainComponent::class) {
        // attributes
        attrs.store = store
        attrs.minCapacity = config.minCapacity
        attrs.maxCapacity = config.maxCapacity

        // actions
        attrs.onRemoveGlass = { store.dispatch(RemoveGlassAction) }
        attrs.onAddGlass = {
            val capacity = (config.minCapacity..config.maxCapacity).random()
            val current = (0..capacity).random()
            val glass = Glass(current = current, capacity = capacity)
            store.dispatch(AddGlassAction(glass))
        }

        attrs.onFinalCurrentChange = { index, current ->
            store.dispatch(
                SetGlassCurrent(
                    isInitialState = false,
                    glassIndex = index,
                    newCurrentValue = current
                ))
        }

        attrs.onInitialCurrentChange = { index, current ->
            store.dispatch(
                SetGlassCurrent(
                    isInitialState = true,
                    glassIndex = index,
                    newCurrentValue = current
                ))
        }
        attrs.onInitialCapacityChange = { index, capacity ->
            store.dispatch(SetGlassCapacity(index to capacity))
        }

        attrs.onSolve = {
            val initial = store.state.initialState
            val final = store.state.finalState
            store.dispatch(SolveAction(initial to final))
        }
    }