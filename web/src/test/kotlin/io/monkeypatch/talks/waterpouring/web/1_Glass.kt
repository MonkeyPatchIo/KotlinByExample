package io.monkeypatch.talks.waterpouring.web

import io.monkeypatch.talks.waterpouring.model.Glass
import io.monkeypatch.talks.waterpouring.model.toGlass
import io.monkeypatch.talks.waterpouring.web.test.shouldBe
import mocha.describe
import mocha.invoke
import mocha.it
import model.empty
import model.fill
import model.fillPercent
import model.minus
import model.plus
import model.remainingVolume
import kotlin.math.round


val testGlass = describe("[1] Glass") {

    describe("1. remainingVolume") {
        it("should be capacity when empty") {
            val emptyGlass = "0/10".toGlass()

            val remainingVolume = emptyGlass.remainingVolume()

            remainingVolume shouldBe emptyGlass.capacity
        }
        it("should be 0 when full") {
            val fullGlass = "10/10".toGlass()

            val remainingVolume = fullGlass.remainingVolume()

            remainingVolume shouldBe 0
        }
        it("should be (capacity - current) otherwise") {
            val glass = "2/10".toGlass()

            val remainingVolume = glass.remainingVolume()

            remainingVolume shouldBe (glass.capacity - glass.current)
        }
    }

    describe("2. empty") {
        it("should empty a glass") {
            val glass = "5/10".toGlass()

            val empty = glass.empty()

            empty.capacity shouldBe glass.capacity
            empty.current shouldBe 0
        }
    }

    describe("3. fill") {
        it("should fill a glass") {
            val glass = "5/10".toGlass()

            val full = glass.fill()

            full.capacity shouldBe glass.capacity
            full.current shouldBe glass.capacity
        }
    }

    describe("4. fillPercent") {
        it("should be 0 when empty") {
            val emptyGlass = "0/10".toGlass()

            val round = round(emptyGlass.fillPercent())

            round shouldBe 0.0
        }

        it("should be 100 when full") {
            val fullGlass = "10/10".toGlass()

            val round = round(fullGlass.fillPercent())

            round shouldBe 100.0
        }

        it("should be percent otherwise") {
            val glass = "2/10".toGlass()

            val round = round(glass.fillPercent())

            round shouldBe 20.0
        }
    }

    describe("5. minus") {

        it("should remove value to glass") {
            val glass = "5/10".toGlass()

            val minus = glass.minus(2)

            minus.current shouldBe (glass.current - 2)
            minus.capacity shouldBe glass.capacity

        }
        // Cannot test operator 😢
        it.skip("should be an operator") {
            val glass = "5/10".toGlass()

            val minus = eval("glass - 2") as Glass

            minus.current shouldBe (glass.current - 2)
            minus.capacity shouldBe glass.capacity
        }

        it("should handle underflow") {

            val glass = "5/10".toGlass()

            val minus = glass.minus(7)

            minus.current shouldBe 0
            minus.capacity shouldBe glass.capacity
        }
    }


    describe("6. plus") {

        it("should add value to glass") {
            val glass = "5/10".toGlass()

            val plus = glass.plus(2)

            plus.current shouldBe (glass.current + 2)
            plus.capacity shouldBe glass.capacity

        }
        // Cannot test operator 😢
        it.skip("should be an operator") {
            val glass = "5/10".toGlass()

            val plus = eval("glass + 2") as Glass

            plus.current shouldBe (glass.current + 2)
            plus.capacity shouldBe glass.capacity
        }

        it("should handle overflow") {
            val glass = "5/10".toGlass()

            val plus = glass.plus(7)

            plus.current shouldBe glass.capacity
            plus.capacity shouldBe glass.capacity
        }
    }
}