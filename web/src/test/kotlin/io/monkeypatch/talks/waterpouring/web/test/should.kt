package io.monkeypatch.talks.waterpouring.web.test

import io.monkeypatch.talks.waterpouring.model.Glass
import io.monkeypatch.talks.waterpouring.model.Move
import io.monkeypatch.talks.waterpouring.model.State
import model.toDisplayString
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLSpanElement
import store.Action
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Testing any value with equals
 */
infix fun <T> T.shouldBe(value: T) {
    assertEquals(value, this, "should be '$value' instead of '$this'")
}

/**
 * Matching with predicate
 *
 * @param predicate the predicate
 */
infix fun <T> T.shouldMatch(predicate: (T) -> Boolean) {
    assertTrue("should match $predicate") {
        predicate(this)
    }
}

/**
 * Testing a null value
 */
infix fun <T : Any> T?.shouldNotBeNull(message: String): T {
    assertNotNull(this, message)
    return this!!
}

/**
 * Testing a [Collection] size
 *
 * @param size the expected size
 */
infix fun <T> Collection<T>.shouldHaveSize(size: Int) {
    assertEquals(size, this.size, "should have size '$size' instead of '${this.size}'")
}

/**
 * Testing an [Action]
 */
infix fun <P> Action<P>.shouldHavePayload(predicate: (P) -> Boolean) {
    assertTrue("should have payload matching $predicate") {
        predicate(this.payload)
    }
}

/**
 * Testing [Action] dispatched
 */
inline fun <S, reified A> MockStore<S>.shouldHaveAction(): A {
    val action = this.actions.filterIsInstance<A>().firstOrNull()
    assertTrue("should have action matching predicate ${A::class}, got $actions") {
        action != null
    }
    return action!!
}

/**
 * Testing has a CSS class
 *
 * @param expectedClass the expected CSS class
 */
infix fun HTMLElement.shouldHasClass(expectedClass: String) {
    assertTrue("should have '$expectedClass' CSS class") {
        classList.contains(expectedClass)
    }
}

/**
 * Testing contains an [HTMLElement] by selector
 *
 * @param selector the selector
 * @return the element
 */
inline infix fun <reified T : HTMLElement> HTMLElement.shouldContainElement(selector: String): T {
    val res = querySelector(selector) as? T
    assertNotNull(res, "should contains '$selector'")
    return res!!
}

/**
 * Testing the text of an [HTMLElement]
 *
 * @param text the expected text
 */
infix fun HTMLElement.shouldHaveText(text: String) {
    val actual = textContent?.trim()
    assertEquals(text.trim(), actual, "should have text '$text' instead of '$actual'")
}

/**
 * Testing the tag of an [HTMLElement]
 *
 * @param tagName the tag
 */
infix fun HTMLElement.shouldBeTag(tagName: String) {
    val expected = tagName.toUpperCase()
    assertEquals(expected, this.tagName, "tag should be '$expected' instead of '${this.tagName}'")
}

/**
 * Testing an input value
 *
 * @param value the expected value
 */
infix fun HTMLInputElement.shouldHaveValue(value: String) {
    assertEquals(value, this.value, "input should have value '$value' instead of '${this.value}")
}

/**
 * Testing an input min
 *
 * @param value the expected minimum value
 */
infix fun HTMLInputElement.shouldHaveMin(value: Number) {
    assertEquals(value.toString(), this.min, "input should have value '$value' instead of '${this.min}")
}

/**
 * Testing an input max
 *
 * @param value the expected maximum value
 */
infix fun HTMLInputElement.shouldHaveMax(value: Number) {
    assertEquals(value.toString(), this.max, "input should have value '$value' instead of '${this.max}")
}

/**
 * Testing a [String] contains a value
 *
 * @param value the contained value
 */
infix fun String.shouldContain(value: String) {
    assertTrue("'$this' should contains '$value'") {
        this.contains(value)
    }
}

/**
 * Test [Glass] template
 *
 * @param glass the glass
 */
infix fun HTMLElement.shouldBeGlass(glass: Glass) {
    this shouldBeTag "div"
    this shouldHasClass "glass"

    this.style.height shouldBe "${glass.capacity}em"
    // FIXME   this.style.backgroundImage shouldContain " ${glass.fillPercent()}%"
    // is not working ???

}

/**
 * Test [Move]
 * @param move the expected move
 */
infix fun HTMLElement.shouldBeMove(move: Move?) {
    this shouldBeTag "div"
    this shouldHasClass "move"
    if (move != null) {
        this shouldHaveText move.toDisplayString()
    }
}

/**
 * Test solution element
 * @param pair the expected [Move]? and the [State]
 */
infix fun HTMLElement.shouldBeSolutionElement(pair: Pair<Move?, State>) {
    this shouldBeTag "li"

    val (move, state) = pair

    val moveNode: HTMLDivElement = this shouldContainElement ".move"
    moveNode shouldBeMove move

    val stateNode: HTMLDivElement = this shouldContainElement ".state"
    stateNode.childNodes.asIterable().zip(state).forEach { (glassContainerNode, glass) ->
        glassContainerNode shouldBeTag "div"
        glassContainerNode shouldHasClass "glass-container"

        val glassNode: HTMLDivElement = glassContainerNode shouldContainElement ".glass"
        glassNode.shouldBeGlass(glass)

        val span: HTMLSpanElement = glassContainerNode shouldContainElement "span"
        span shouldBeTag "span"
        span shouldHaveText "${glass.current} / ${glass.capacity}"
    }
}

