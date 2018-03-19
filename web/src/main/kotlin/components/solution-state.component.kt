package components

import io.monkeypatch.talks.waterpouring.model.Move
import io.monkeypatch.talks.waterpouring.model.State
import model.toDisplayString
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.ReactElement
import react.dom.div
import react.dom.li
import react.dom.span


/**
 * Create React component a solution [State]:
 *
 * ```html
 * <li>
 *     <div class="move"> <!-- move --></div>
 *     <div class="state">
 *         <div class="glass-container">
 *             <!-- glass -->
 *             <span><!-- current -->/ <!-- capacity --></span>
 *         </div>
 *     </div>
 * </li>
 * <!-- ... -->
 * ```
 */

interface SolutionStateProps : RProps {
    var solutionList: List<Pair<Move?, State>>
}

class SolutionStateComponent : RComponent<SolutionStateProps, RState>() {

    override fun RBuilder.render() {
        props.solutionList.map { (move, state) ->
            li {
                div(classes = "move") {
                    +(move?.toDisplayString() ?: "")
                }
                div(classes = "state") {
                    state.map { glass ->
                        div(classes = "glass-container") {
                            glass(glass)
                            span { +"${glass.current} / ${glass.capacity}" }
                        }
                    }
                }
            }
        }
    }
}

fun RBuilder.solutionState(solutionList: List<Pair<Move?, State>>): ReactElement =
    child(SolutionStateComponent::class) { attrs.solutionList = solutionList }
