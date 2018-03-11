package components

import io.monkeypatch.talks.waterpouring.model.State
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.ReactElement


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
        TODO("2.3: template")
        TODO("3.4: changer la valeur courante dans l'état initial")
        TODO("3.5: changer la capacité dans l'état initial")
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
