package io.monkeypatch.talks.waterpouring.model

/**
 * A Glass POJO
 */
data class Glass(val capacity: Int, val current: Int = 0) {

    init {
        require(capacity > 0) { "Capacity: $capacity should be > 0" }
        require(current in 0..capacity) { "Current: $current should be into [0, $capacity]" }
    }
}

/**
 * Define a State like an alias of List<Glass>
 */
typealias State = List<Glass>

/**
 * All Moves
 */
sealed class Move

/** Empty a glass */
data class Empty(val index: Int) : Move()

/** Fill a glass */
data class Fill(val index: Int) : Move()

/** Pour a glass into another glass */
data class Pour(val from: Int, val to: Int) : Move() {
    init {
        require(from != to)
    }
}

