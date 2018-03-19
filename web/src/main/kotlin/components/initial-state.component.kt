package components

import io.monkeypatch.talks.waterpouring.model.State
import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import org.w3c.dom.HTMLInputElement
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.ReactElement
import react.dom.div
import react.dom.input
import react.dom.span


/**
 * Create content for initial [State]
 *
 * ```html
 * <div class="glass-container">
 *     <!-- glass -->
 *     <input type="number" value="<the glass current>" min="0" max="<the class capacity>">
 *     <span>/</span>
 *     <input type="number" value="<the glass capacity>" min="<the configuration min capacity value>" max="<the configuration max capacity value>">
 * </div>
 * <!-- ... -->
 * ```
 */

interface InitialStateProps : RProps {
    var state: State
    var minCapacity: Int
    var maxCapacity: Int
    var onInitialCapacityChange: (Int, Int) -> Unit
    var onInitialCurrentChange: (Int, Int) -> Unit
}

class InitialStateComponent : RComponent<InitialStateProps, RState>() {
    override fun RBuilder.render() {

        props.state.mapIndexed { index, glass ->
            div(classes = "glass-container") {
                glass(glass)
                input(type = InputType.number, classes = "current") {
                    attrs {
                        min = "0"
                        value = "${glass.current}"
                        max = "${glass.capacity}"
                        onChangeFunction = { event ->
                            val newCurrent = (event.target as? HTMLInputElement)
                                ?.value
                                ?.toInt()
                                    ?: throw IllegalStateException("Expected an input")
                            props.onInitialCurrentChange(index, newCurrent)
                        }
                    }
                }
                span { +"/" }
                input(type = InputType.number, classes = "capacity") {
                    attrs {
                        min = "${props.minCapacity}"
                        value = "${glass.capacity}"
                        max = "${props.maxCapacity}"
                        onChangeFunction = { event ->
                            val newCapacity = (event.target as? HTMLInputElement)
                                ?.value
                                ?.toInt()
                                    ?: throw IllegalStateException("Expected an input")
                            props.onInitialCapacityChange(index, newCapacity)
                        }
                    }
                }
            }
        }
    }
}

fun RBuilder.initialState(state: State,
                          minCapacity: Int,
                          maxCapacity: Int,
                          onInitialCapacityChange: (Int, Int) -> Unit,
                          onInitialCurrentChange: (Int, Int) -> Unit): ReactElement =
    child(InitialStateComponent::class) {
        attrs.state = state
        attrs.minCapacity = minCapacity
        attrs.maxCapacity = maxCapacity
        attrs.onInitialCapacityChange = onInitialCapacityChange
        attrs.onInitialCurrentChange = onInitialCurrentChange
    }
