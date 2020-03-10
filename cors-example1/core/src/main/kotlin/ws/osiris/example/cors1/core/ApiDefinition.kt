package ws.osiris.example.cors1.core

import ws.osiris.core.ComponentsProvider
import ws.osiris.core.HttpHeaders
import ws.osiris.core.HttpMethod
import ws.osiris.core.api

/** The API. */
val api = api<ComponentsProvider>(cors = true) {

    cors {
        allowMethods = setOf(HttpMethod.GET, HttpMethod.POST)
        allowOrigin = setOf("*")
        allowHeaders = setOf(HttpHeaders.CONTENT_TYPE)
    }

    get("/foo") {
        mapOf("message" to "hello, world!")
    }

    post("/bar") {
        mapOf("message" to "hello, world!")
    }
}

/**
 * Creates the components used by the example API.
 */
fun createComponents(): ComponentsProvider = object : ComponentsProvider {}
