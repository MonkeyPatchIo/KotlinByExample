package io.monkeypatch.talks.mobile.waterpouring

import io.monkeypatch.talks.mobile.waterpouring.model.*
import io.monkeypatch.talks.mobile.waterpouring.test.shouldBe
import io.monkeypatch.talks.waterpouring.model.toGlass
import org.junit.Test

@Suppress("FunctionName")
class GlassTest {

    @Test
    fun _1_1_Glass_remainingVolume_Empty() {
        val emptyGlass = "0/10".toGlass()
        emptyGlass.remainingVolume() shouldBe emptyGlass.capacity
    }

    @Test
    fun _1_1_Glass_remainingVolume_Full() {
        val fullGlass = "10/10".toGlass()
        fullGlass.remainingVolume() shouldBe 0
    }

    @Test
    fun _1_1_Glass_remainingVolume_Normal() {
        val glass = "2/10".toGlass()
        glass.remainingVolume() shouldBe (glass.capacity - glass.current)
    }

    @Test
    fun _1_2_Glass_filled_Empty() {
        val emptyGlass = "0/10".toGlass()
        emptyGlass.filled() shouldBe 0.0f
    }

    @Test
    fun _1_2_Glass_filled_Full() {
        val fullGlass = "10/10".toGlass()
        fullGlass.filled() shouldBe 1.0f
    }

    @Test
    fun _1_2_Glass_filled_normal() {
        val glass = "2/10".toGlass()
        glass.filled() shouldBe 0.2f
    }

    @Test
    fun _1_3_Glass_sized_Full() {
        val fullGlass = "0/${Configuration.maxCapacity}".toGlass()
        fullGlass.sized() shouldBe 1.0f
    }

    @Test
    fun _1_3_Glass_sized_min() {
        val fullGlass = "0/1".toGlass()
        fullGlass.sized() shouldBe (1.0f / Configuration.maxCapacity)
    }

    @Test
    fun _1_4_Glass_empty() {
        val glass = "5/10".toGlass()

        val empty = glass.empty()

        empty.capacity shouldBe glass.capacity
        empty.current shouldBe 0
    }

    @Test
    fun _1_5_Glass_fill() {
        val glass = "5/10".toGlass()

        val full = glass.fill()

        full.capacity shouldBe glass.capacity
        full.current shouldBe glass.capacity
    }

    @Test
    fun _1_6_Glass_minus() {
        val glass = "5/10".toGlass()

        glass.minus(2).current shouldBe (glass.current - 2)
        glass.minus(2).capacity shouldBe glass.capacity
    }

    @Test
    fun _1_6_Glass_minus_underflow() {
        val glass = "5/10".toGlass()
        glass.minus(7).current shouldBe 0
    }

    @Test
    fun _1_7_Glass_plus() {
        val glass = "5/10".toGlass()

        glass.plus(2).current shouldBe (glass.current + 2)
        glass.plus(2).capacity shouldBe glass.capacity
    }

    @Test
    fun _1_7_Glass_plus_overflow() {
        val glass = "5/10".toGlass()
        glass.plus(7).current shouldBe glass.capacity
    }

}
