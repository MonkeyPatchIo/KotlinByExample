package io.monkeypatch.talks.waterpouring.server

import io.kotlintest.shouldBe
import io.kotlintest.specs.ShouldSpec
import io.monkeypatch.talks.waterpouring.model.Glass
import io.monkeypatch.talks.waterpouring.model.empty
import io.monkeypatch.talks.waterpouring.model.fill
import io.monkeypatch.talks.waterpouring.model.isEmpty
import io.monkeypatch.talks.waterpouring.model.isFull
import io.monkeypatch.talks.waterpouring.model.minus
import io.monkeypatch.talks.waterpouring.model.plus
import io.monkeypatch.talks.waterpouring.model.remainingVolume
import io.monkeypatch.talks.waterpouring.model.toGlass


class GlassTests : ShouldSpec() {

    init {
        val (emptyGlass, glass, fullGlass) = listOf("0/10", "5/10", "10/10").map { it.toGlass() }

        "1.1/ Glass.isEmpty()" {

            should("return true for an empty Glass") {
                emptyGlass.isEmpty() shouldBe true
            }

            should("return false for a not empty Glass") {
                glass.isEmpty() shouldBe false
            }
        }

        "1.2/ Glass.isFull()" {

            should("return true for a full Glass") {
                fullGlass.isFull() shouldBe true
            }

            should("return false for a not full Glass") {
                glass.isFull() shouldBe false
            }
        }

        "1.3/ Glass.remainingVolume()" {

            should("return capacity for an empty Glass") {
                emptyGlass.remainingVolume() shouldBe emptyGlass.capacity
            }

            should("return 0 for an fill Glass") {
                fullGlass.remainingVolume() shouldBe 0
            }

            should("return remaining for a normal Glass") {
                glass.remainingVolume() shouldBe (glass.capacity - glass.current)
            }
        }

        "1.4/ Glass.empty()" {

            should("return an empty Glass") {
                glass.empty().current shouldBe 0
            }

            should("keep an the Glass capacity") {
                glass.empty().capacity shouldBe glass.capacity
            }
        }

        "1.5/ Glass.fill()" {

            should("return an fill Glass") {
                glass.fill().current shouldBe glass.capacity
            }

            should("keep an the Glass capacity") {

                glass.fill().capacity shouldBe glass.capacity
            }
        }

        "1.6/ Glass.minus(Int)" {

            should("return glass with subtracted current") {
                glass.minus(2).current shouldBe (glass.current - 2)
            }

            should("keep the Glass capacity") {
                glass.minus(2).capacity shouldBe glass.capacity
            }

            should("keep an the Glass current greater or equal than 0") {
                glass.minus(7).current shouldBe 0
            }

        }

        "1.7/ Glass.plus(Int)" {

            should("return glass with added current") {
                glass.plus(2).current shouldBe (glass.current + 2)
            }

            should("keep an the Glass capacity") {
                glass.plus(2).capacity shouldBe glass.capacity
            }

            should("keep an the Glass current lower or equal than capacity") {
                glass.plus(7).current shouldBe glass.capacity
            }

        }
    }
}
