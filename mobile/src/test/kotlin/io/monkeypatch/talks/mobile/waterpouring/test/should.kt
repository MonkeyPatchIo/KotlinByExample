package io.monkeypatch.talks.mobile.waterpouring.test

import org.junit.Assert.*


/**
 * Testing any value with equals
 */
infix fun Float.shouldBe(value: Float) {
    assertTrue("should be '$value' instead of '$this'", value.toString() == this.toString())
}
/**
 * Testing any value with equals
 */
infix fun <T> T.shouldBe(value: T) {
    assertTrue("should be '$value' instead of '$this'", value == this)
}

/**
 * Matching with predicate
 *
 * @param predicate the predicate
 */
infix fun <T> T.shouldMatch(predicate: (T) -> Boolean) {
    assertTrue("should match $predicate", predicate(this))
}

/**
 * Testing a null value
 */
infix fun <T : Any> T?.shouldNotBeNull(message: String): T {
    assertNotNull(message, this)
    return this!!
}

/**
 * Testing a [Collection] size
 *
 * @param size the expected size
 */
infix fun <T> Collection<T>.shouldHaveSize(size: Int) {
    assertEquals("should have size '$size' instead of '${this.size}'", size, this.size)
}

/**
 * Testing a [String] contains a value
 *
 * @param value the contained value
 */
infix fun String.shouldContain(value: String) {
    assertTrue("'$this' should contains '$value'", this.contains(value))
}
