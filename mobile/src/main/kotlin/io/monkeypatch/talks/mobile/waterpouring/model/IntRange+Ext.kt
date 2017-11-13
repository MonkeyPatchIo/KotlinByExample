package io.monkeypatch.talks.mobile.waterpouring.model

import java.util.*


private val rand = Random()

/**
 * Create a random [Int] in a range
 */
fun IntRange.random(): Int {
    val diff = last - first
    return rand.nextInt(diff) + first + 1
}