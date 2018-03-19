package model

import io.monkeypatch.talks.waterpouring.model.Empty
import io.monkeypatch.talks.waterpouring.model.Fill
import io.monkeypatch.talks.waterpouring.model.Move
import io.monkeypatch.talks.waterpouring.model.Pour
import io.monkeypatch.talks.waterpouring.model.State


// State extension
/**
 * Apply a [Move] to a [State]
 *
 * @param move the [Move] to apply
 * @return the new [State]
 */
fun State.move(move: Move): State =
    mapIndexed { index, glass ->
        when (move) {
            is Empty ->
                if (index == move.index) glass.empty() else glass
            is Fill  ->
                if (index == move.index) glass.fill() else glass
            is Pour  ->
                when (index) {
                    move.from -> glass - this[move.to].remainingVolume()
                    move.to   -> glass + this[move.from].current
                    else      -> glass
                }
        }
    }