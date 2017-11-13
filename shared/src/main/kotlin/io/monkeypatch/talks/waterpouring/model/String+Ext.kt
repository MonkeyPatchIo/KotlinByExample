package io.monkeypatch.talks.waterpouring.model



/**
 * Create a Glass from a [String]
 */
fun String.toGlass(): Glass {
    val match = "(\\d*)/(\\d*)".toRegex().matchEntire(this) ?: throw IllegalArgumentException("Does not match x/y pattern")
    val current = match.groups[1]?.value?.toInt() ?: throw IllegalArgumentException("Does not match x/y capacity")
    val capacity = match.groups[2]?.value?.toInt() ?: throw IllegalArgumentException("Does not match x/y capacity")
    return Glass(capacity = capacity, current = current)
}


/**
 * Create a State from a [String]
 */
fun String.toState(): State =
    this.split(",")
        .map { it.trim() }
        .map { it.toGlass() }