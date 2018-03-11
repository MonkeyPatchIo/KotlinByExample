package helpers

import kotlin.js.Math
import kotlin.math.ceil

/**
 * Create a random [Int] in a range
 */
fun IntRange.random(): Int {
    val diff = last - first
    val rand = Math.random() * diff.toDouble() + first // Math.random() not yet available into kotlin.math.XXX
    return ceil(rand).toInt()
}