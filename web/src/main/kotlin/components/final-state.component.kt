package components

import io.monkeypatch.talks.waterpouring.model.State
import kotlinx.html.InputType.number
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
 * Create content for final [State]
 *
 * ```html
 * <div class="glass-container">
 *     <!-- glass -->
 *     <input type="number" value="<the glass current>" min="0" max="<the class capacity>">
 *     <span>/</span>
 *     <span class="capacity"><!-- capacity --></span>
 * </div>
 * <!-- ... -->
 * ```
 */

interface FinalStateProps : RProps {
    var state: State
    var onFinalCurrentChange: (Int, Int) -> Unit
}

class FinalStateComponent : RComponent<FinalStateProps, RState>() {
    override fun RBuilder.render() {
        props.state.mapIndexed { index, glass ->
            div(classes = "glass-container") {
                glass(glass)
                input(type = number, classes = "current") {
                    attrs {
                        min = "0"
                        value = "${glass.current}"
                        max = "${glass.capacity}"
                        onChangeFunction = { event ->
                            val newCurrent = (event.target as? HTMLInputElement)
                                ?.value
                                ?.toInt()
                                    ?: throw IllegalStateException("Expected an input")
                            props.onFinalCurrentChange(index, newCurrent)
                        }
                    }
                }
                span { +"/" }
                span(classes = "capacity") { +"${glass.capacity}" }
            }
        }
    }
}


fun RBuilder.finalState(state: State, onFinalCurrentChange: (Int, Int) -> Unit): ReactElement =
    child(FinalStateComponent::class) {
        attrs.state = state
        attrs.onFinalCurrentChange = onFinalCurrentChange
    }