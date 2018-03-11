package components

import io.monkeypatch.talks.waterpouring.model.Move
import io.monkeypatch.talks.waterpouring.model.State
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.ReactElement


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
        TODO("4.6")
    }
}

fun RBuilder.solutionState(solutionList: List<Pair<Move?, State>>): ReactElement =
    child(SolutionStateComponent::class) { attrs.solutionList = solutionList }
