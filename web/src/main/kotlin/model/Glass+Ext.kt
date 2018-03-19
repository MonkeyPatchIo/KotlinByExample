package model

import io.monkeypatch.talks.waterpouring.model.Glass


// Glass extensions
/**
 * Compute remaining volume of a [Glass].
 *
 * @return remaining volume
 */
fun Glass.remainingVolume(): Int =
    capacity - current

/**
 * Empty a [Glass].
 *
 * We are immutable, so we return a new instance of a [Glass]
 *
 * @return a empty [Glass]
 */
fun Glass.empty(): Glass = TODO("1.2")

/**
 * Fill a [Glass].
 *
 * We are immutable, so we return a new instance of a [Glass]
 *
 * @return a filled [Glass]
 */
fun Glass.fill(): Glass = TODO("1.3")

/**
 * Compute fill percentage
 *
 * @return fill percentage
 */
fun Glass.fillPercent(): Double = TODO("1.4")

/**
 * Remove content to the [Glass].
 *
 * <!> you cannot remove more than current content of [Glass]
 * We are immutable, so we return a new instance of a [Glass].
 *
 * @return the new [Glass]
 */
/* TODO("1.5") */fun Glass.minus(value: Int): Glass = TODO("1.5")

/**
 * Add some content to the [Glass].
 *
 * <!> the [Glass] should not spill (maximum value is capacity)
 * We are immutable, so we return a new instance of a [Glass].
 *
 * @return the new [Glass]
 */
/* TODO("1.6") */fun Glass.plus(value: Int): Glass = TODO("1.6")
