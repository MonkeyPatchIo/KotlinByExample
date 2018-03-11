package io.monkeypatch.talks.waterpouring.server

import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.stubbing.OngoingStubbing

// Reified version of mock
inline fun <reified T> mock(): T =
    mock(T::class.java)

// Alias for `when`
fun <A> whenCalling(methodCall: A): OngoingStubbing<A> =
    Mockito.`when`(methodCall)
