package io.monkeypatch.talks.waterpouring.model


/**
 * Is the [Glass] is empty ?
 *
 * @return `true` if the [Glass] is empty, `false` otherwise
 */
fun Glass.isEmpty(): Boolean =
    current == 0

/**
 * Is the [Glass] is full ?
 *
 * @return `true` if the [Glass] is full, `false` otherwise
 */
fun Glass.isFull(): Boolean =
    current == capacity

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
fun Glass.empty(): Glass =
    copy(current = 0)

/**
 * Fill a [Glass].
 *
 * We are immutable, so we return a new instance of a [Glass]
 *
 * @return a filled [Glass]
 */
fun Glass.fill(): Glass =
    copy(current = capacity)

/**
 * Remove content to the [Glass].
 *
 * <!> you cannot remove more than current content of [Glass]
 * We are immutable, so we return a new instance of a [Glass].
 *
 * @return the new [Glass]
 */
operator fun Glass.minus(value: Int): Glass =
    copy(current = (current - value).coerceAtLeast(0))

/**
 * Add some content to the [Glass].
 *
 * <!> the [Glass] should not spill (maximum value is capacity)
 * We are immutable, so we return a new instance of a [Glass].
 *
 * @return the new [Glass]
 */
operator fun Glass.plus(value: Int): Glass =
    copy(current = (current + value).coerceAtMost(capacity))