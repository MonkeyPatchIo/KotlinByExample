package components

import io.monkeypatch.talks.waterpouring.model.Glass
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.ReactElement

/**
 * Create React component for a [Glass]:
 *
 * ```html
 * <div class="glass" style="..."><div>
 * ```
 * With extra style attributes:
 *  - height = <glass.capacity>em
 *  - background-image = linear-gradient(to top, #e2b65e <glass.fillPercent()>%, transparent 0px)
 */

interface RGlassProps : RProps {
    var glass: Glass
}

class GlassComponent : RComponent<RGlassProps, RState>() {
    override fun RBuilder.render() {
        TODO("2.1")
    }
}

fun RBuilder.glass(glass: Glass): ReactElement =
    child(GlassComponent::class) { attrs.glass = glass }