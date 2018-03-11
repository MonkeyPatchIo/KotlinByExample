package io.monkeypatch.talks.waterpouring.server.utils

import java.time.Duration


val Int.second: Duration
    get() = this.seconds

val Int.seconds: Duration
    get() = Duration.ofSeconds(this.toLong())

val Long.second: Duration
    get() = this.seconds

val Long.seconds: Duration
    get() = Duration.ofSeconds(this)
