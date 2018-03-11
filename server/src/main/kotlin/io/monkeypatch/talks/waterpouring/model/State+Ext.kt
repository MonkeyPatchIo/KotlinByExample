package io.monkeypatch.talks.waterpouring.model

/**
 * An alias to store a [State] with his history.
 * We use a [Pair]
 */
typealias StateWithHistory = Pair<State, List<Move>>


/**
 * Compute all available [Move]s from a [State]
 *
 * @return all available [Move]s
 */
fun State.availableMoves(): Collection<Move> {
    val glassNotEmptyIndexes: List<Int> =
        mapIndexed { index, glass -> index to glass }
            .filterNot { (_, glass) -> glass.isEmpty() }
            .map { it.first }

    val glassNotFillIndexes: List<Int> =
        mapIndexed { index, glass -> index to glass }
            .filterNot { (_, glass) -> glass.isFull() }
            .map { it.first }

    val empties: List<Move> = glassNotEmptyIndexes.map { Empty(it) }
    val fills: List<Move> = glassNotFillIndexes.map { Fill(it) }
    val pours: List<Move> = glassNotEmptyIndexes
        .flatMap { from ->
            glassNotFillIndexes
                .filter { from != it }
                .map { to -> Pour(from, to) }
        }

    return empties + fills + pours
}


/**
 * Apply a [Move] to a current [State]
 *
 * @param move the [Move] to process
 * @return the new [State]
 */
fun State.process(move: Move): State =
    this.mapIndexed { index, glass ->
        TODO("2.3")
    }


/**
 * Display String
 *
 * @return a String representation for display the [State]
 */
fun State.toDisplayString(): String =
    this.joinToString(", ", prefix = "[", postfix = "]") { "${it.current}/${it.capacity}" }
