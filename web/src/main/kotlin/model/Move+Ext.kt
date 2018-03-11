package model

import io.monkeypatch.talks.waterpouring.model.Empty
import io.monkeypatch.talks.waterpouring.model.Fill
import io.monkeypatch.talks.waterpouring.model.Move
import io.monkeypatch.talks.waterpouring.model.Pour


// Move extensions
/**
 * Create a display [String] for [Move]
 */
fun Move.toDisplayString(): String =
    when (this) {
        is Empty -> "Vider #${index + 1}"
        is Fill  -> "Remplir #${index + 1}"
        is Pour  -> "Verser #${from + 1} dans #${to + 1}"
    }