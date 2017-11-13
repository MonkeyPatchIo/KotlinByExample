@file:Suppress("ClassName")

package io.monkeypatch.talks.waterpouring.server

import io.monkeypatch.talks.waterpouring.model.*
import org.amshove.kluent.shouldEqual
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class GlassTests {

    @Nested
    @DisplayName("1.1/ Glass.isEmpty()")
    class Glass_isEmpty {
        @Test
        fun `return true for an empty Glass`() {
            val glass = "0/10".toGlass()
            glass.isEmpty() shouldEqual true
        }

        @Test
        fun `return false for a not empty Glass`() {
            val glass = "5/10".toGlass()
            glass.isEmpty() shouldEqual false
        }
    }

    @Nested
    @DisplayName("1.2/ Glass.isFull()")
    class Glass_isFull {
        @Test
        fun `return true for a full Glass`() {
            val glass = "10/10".toGlass()
            glass.isFull() shouldEqual true
        }

        @Test
        fun `return false for a not full Glass`() {
            val glass = "5/10".toGlass()
            glass.isFull() shouldEqual false
        }
    }

    @Nested
    @DisplayName("1.3/ Glass.remainingVolume()")
    class Glass_remainingVolume {
        @Test
        fun `return capacity for an empty Glass`() {
            val glass = "0/10".toGlass()
            glass.remainingVolume() shouldEqual glass.capacity
        }

        @Test
        fun `return 0 for an fill Glass`() {
            val glass = "10/10".toGlass()
            glass.remainingVolume() shouldEqual 0
        }

        @Test
        fun `return remaining for a normal Glass`() {
            val glass = "2/10".toGlass()
            glass.remainingVolume() shouldEqual (glass.capacity - glass.current)
        }
    }

    @Nested
    @DisplayName("1.4/ Glass.empty()")
    class Glass_empty {
        @Test
        fun `return an empty Glass`() {
            val glass = "5/10".toGlass()
            glass.empty().current shouldEqual 0
        }

        @Test
        fun `keep an the Glass capacity`() {
            val glass = "5/10".toGlass()
            glass.empty().capacity shouldEqual glass.capacity
        }
    }

    @Nested
    @DisplayName("1.5/ Glass.fill()")
    class Glass_fill {
        @Test
        fun `return an fill Glass`() {
            val glass = "5/10".toGlass()
            glass.fill().current shouldEqual glass.capacity
        }

        @Test
        fun `keep an the Glass capacity`() {
            val glass = "5/10".toGlass()
            glass.fill().capacity shouldEqual glass.capacity
        }
    }

    @Nested
    @DisplayName("1.6/ Glass.minus(Int)")
    class Glass_minus {
        @Test
        fun `return glass with subtracted current`() {
            val glass = "5/10".toGlass()
            glass.minus(2).current shouldEqual (glass.current - 2)
        }

        @Test
        fun `keep an the Glass current greater or equal than 0`() {
            val glass = "5/10".toGlass()
            glass.minus(7).current shouldEqual 0
        }

        @Test
        fun `be callable with '-'`() {
            Glass::minus.isOperator shouldEqual true
        }
    }

    @Nested
    @DisplayName("1.7/ Glass.plus(Int)")
    class Glass_plus {
        @Test
        fun `return glass with added current`() {
            val glass = "5/10".toGlass()
            glass.plus(2).current shouldEqual (glass.current + 2)
        }

        @Test
        fun `keep an the Glass current lower or equal than capacity`() {
            val glass = "5/10".toGlass()
            glass.plus(7).current shouldEqual glass.capacity
        }

        @Test
        fun `be callable with '+'`() {
            Glass::plus.isOperator shouldEqual true
        }
    }
}