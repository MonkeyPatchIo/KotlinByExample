package io.monkeypatch.talks.mobile.waterpouring.model

import io.monkeypatch.talks.mobile.waterpouring.Configuration
import io.monkeypatch.talks.waterpouring.model.Glass


/**
 * Compute remaining volume of a [Glass].
 *
 * @return remaining volume
 */
fun Glass.remainingVolume(): Int = TODO("1.1")

/**
 * Compute the percentage filled
 *
 * @return the percentage filled of a [Glass]
 */
fun Glass.filled(): Float = TODO("1.2")

/**
 * Compute the percentage between glass capacity and max glass capacity
 *
 * @return the percentage filled of a [Glass]
 */
fun Glass.sized(): Float = TODO("1.3")

/**
 * Empty a [Glass].
 *
 * We are immutable, so we return a new instance of a [Glass]
 *
 * @return a empty [Glass]
 */
fun Glass.empty(): Glass = TODO("1.4")

/**
 * Fill a [Glass].
 *
 * We are immutable, so we return a new instance of a [Glass]
 *
 * @return a filled [Glass]
 */
fun Glass.fill(): Glass = TODO("1.5")

/**
 * Remove content to the [Glass].
 *
 * <!> you cannot remove more than current content of [Glass]
 * We are immutable, so we return a new instance of a [Glass].
 *
 * @return the new [Glass]
 */
fun Glass.minus(value: Int): Glass = TODO("1.6")

/**
 * Add some content to the [Glass].
 *
 * <!> the [Glass] should not spill (maximum value is capacity)
 * We are immutable, so we return a new instance of a [Glass].
 *
 * @return the new [Glass]
 */
fun Glass.plus(value: Int): Glass = TODO("1.7")


/**
 * Creates a [Glass] filled with a random value
 *
 * @return the random [Glass]
 */
fun randomGlass(): Glass {
    val capacity = (Configuration.minCapacity..Configuration.maxCapacity).random()
    val current = (0..capacity).random()
    return Glass(capacity = capacity, current = current)
}