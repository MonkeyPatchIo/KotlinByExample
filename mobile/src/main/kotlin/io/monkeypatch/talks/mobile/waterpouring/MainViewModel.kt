package io.monkeypatch.talks.mobile.waterpouring


import io.monkeypatch.talks.mobile.waterpouring.model.filled
import io.monkeypatch.talks.mobile.waterpouring.model.random
import io.monkeypatch.talks.mobile.waterpouring.model.randomGlass
import io.monkeypatch.talks.mobile.waterpouring.model.toSolutionList
import io.monkeypatch.talks.mobile.waterpouring.view.GlassLayout
import io.monkeypatch.talks.mobile.waterpouring.view.OnGlassEvent
import io.monkeypatch.talks.waterpouring.model.Glass
import io.monkeypatch.talks.waterpouring.model.Move
import io.monkeypatch.talks.waterpouring.model.State
import io.monkeypatch.talks.waterpouring.model.toGlass
import kotlin.properties.Delegates

enum class GlassPosition {

    POS_1, POS_2, POS_3, POS_4;
}

/**
 * List of Current/ level
 */
typealias GlassAnimation = List<Pair<Int, Float>>


data class StateAnimation(val moves: List<Move?>,
                          val leftAnimation: GlassAnimation,
                          val rightAnimation: GlassAnimation)

class MainViewModel : OnGlassEvent {

    private var glass1: Glass by Delegates.observable("0/5".toGlass(), { _, _: Glass, newGlass: Glass ->
        notifyGlassChanged(GlassPosition.POS_1, newGlass)
    })

    private var glass2: Glass  by Delegates.observable("0/3".toGlass(), { _, _: Glass, newGlass: Glass ->
        notifyGlassChanged(GlassPosition.POS_2, newGlass)
    })

    private var glass3: Glass  by Delegates.observable("4/5".toGlass(), { _, _: Glass, newGlass: Glass ->
        notifyGlassChanged(GlassPosition.POS_3, newGlass)
    })
    private var glass4: Glass  by Delegates.observable("0/3".toGlass(), { _, _: Glass, newGlass: Glass ->
        notifyGlassChanged(GlassPosition.POS_4, newGlass)
    })

    val initialState: State
        get() = listOf(glass1, glass2)

    val finalState: State
        get() = listOf(glass3, glass4)

    private val layouts = mutableMapOf<GlassPosition, GlassLayout>()

    var resultAnimationToPlay: StateAnimation? = null

    private fun GlassPosition.toGlass(): Glass = when (this) {
        GlassPosition.POS_1 -> glass1
        GlassPosition.POS_2 -> glass2
        GlassPosition.POS_3 -> glass3
        GlassPosition.POS_4 -> glass4
    }

    private fun setGlass(glassPosition: GlassPosition, newGlass: Glass) {
        when (glassPosition) {
            GlassPosition.POS_1 -> glass1 = newGlass
            GlassPosition.POS_2 -> glass2 = newGlass
            GlassPosition.POS_3 -> glass3 = newGlass
            GlassPosition.POS_4 -> glass4 = newGlass
        }
    }

    fun bindViews(glassLayout1: GlassLayout, glassLayout2: GlassLayout, glassLayout3: GlassLayout, glassLayout4: GlassLayout) {
        bindView(GlassPosition.POS_1, glassLayout1)
        bindView(GlassPosition.POS_2, glassLayout2)
        bindView(GlassPosition.POS_3, glassLayout3)
        bindView(GlassPosition.POS_4, glassLayout4)
    }


    private fun bindView(position: GlassPosition, glassLayout: GlassLayout) {
        layouts[position] = glassLayout
        glassLayout.onGlassEvent = this
        glassLayout.glassPosition = position
        glassLayout.glassChanged(position.toGlass())
    }

    private fun notifyGlassChanged(glassPosition: GlassPosition, current: Glass) {
        layouts[glassPosition]?.glassChanged(current)
    }

    override fun onClick(glassPosition: GlassPosition) {
        val previous = glassPosition.toGlass()
        val current =  (previous.current + 1) % (previous.capacity + 1)
        setGlass(glassPosition, previous.copy(current = current))
    }

    override fun onDoubleClick(glassPosition: GlassPosition) {
        val previous = glassPosition.toGlass()

        val newCapacity = ((previous.capacity + 1) % (Configuration.maxCapacity + 1))
                .coerceAtLeast(Configuration.minCapacity)
        val newCurrent = previous.current.coerceIn(Configuration.minCapacity..newCapacity)

        setGlass(glassPosition, Glass(capacity = newCapacity, current = newCurrent))
    }

    /**
     * display result solution.
     */
    fun setResult(result: List<Move>) {

        val solution = result.toSolutionList(initialState)

        // Play Animations
        val glassAnimations: Pair<GlassAnimation, GlassAnimation> = solution.asSequence()
                // A state can only have 2 glass in that context
                .filter { it.second.size == 2 }
                .map { (_, states) ->
                    // precondition: a state has only 2 glass in that case
                    val (firstGlass, secondGlass) = states
                    Pair(firstGlass.current, firstGlass.filled()) to Pair(secondGlass.current, secondGlass.filled())
                }
                .unzip()

        val moves: List<Move?> = solution.map { it.first }

        resultAnimationToPlay = StateAnimation(moves, glassAnimations.first, glassAnimations.second)
    }

    fun shuffle() {
        glass1 = randomGlass()
        glass2 = randomGlass()
        glass3 = glass1.copy(current = (0..glass1.capacity).random())
        glass4 = glass2.copy(current = (0..glass2.capacity).random())
    }
}