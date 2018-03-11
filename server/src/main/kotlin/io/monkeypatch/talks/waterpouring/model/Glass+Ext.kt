package io.monkeypatch.talks.waterpouring.model


/**
 * Is the [Glass] is empty ?
 *
 * @return `true` if the [Glass] is empty, `false` otherwise
 */
fun Glass.isEmpty(): Boolean =
    TODO("1.1")

/**
 * Is the [Glass] is full ?
 *
 * @return `true` if the [Glass] is full, `false` otherwise
 */
fun Glass.isFull(): Boolean =
    TODO("1.2")

/**
 * Compute remaining volume of a [Glass].
 *
 * @return remaining volume
 */
fun Glass.remainingVolume(): Int =
    TODO("1.3")

/**
 * Empty a [Glass].
 *
 * We are immutable, so we return a new instance of a [Glass]
 *
 * @return a empty [Glass]
 */
fun Glass.empty(): Glass =
    TODO("1.4")

/**
 * Fill a [Glass].
 *
 * We are immutable, so we return a new instance of a [Glass]
 *
 * @return a filled [Glass]
 */
fun Glass.fill(): Glass =
    TODO("1.5")

/**
 * Remove content to the [Glass].
 *
 * <!> you cannot remove more than current content of [Glass]
 * We are immutable, so we return a new instance of a [Glass].
 *
 * @return the new [Glass]
 */
/* TODO 1.6 */ fun Glass.minus(value: Int): Glass =
    TODO("1.6")

/**
 * Add some content to the [Glass].
 *
 * <!> the [Glass] should not spill (maximum value is capacity)
 * We are immutable, so we return a new instance of a [Glass].
 *
 * @return the new [Glass]
 */
/* TODO 1.7 */ fun Glass.plus(value: Int): Glass =
    TODO("1.7")