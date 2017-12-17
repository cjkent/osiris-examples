package io.github.cjkent.osiris.example.customauth2.core

import io.github.cjkent.osiris.aws.CustomAuth
import io.github.cjkent.osiris.core.ComponentsProvider
import io.github.cjkent.osiris.core.api

/** The API. */
val api = api(ComponentsProvider::class) {
    get("/helloworld") {
        mapOf("message" to "hello, world!")
    }
    auth(CustomAuth) {
        get("/private") {
            mapOf("message" to "hello, authorised user!")
        }
    }
}

/**
 * Creates the components used by the test API.
 */
fun createComponents(): ComponentsProvider = object : ComponentsProvider {}
