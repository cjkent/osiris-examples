package ws.osiris.example.cors1.core

import ws.osiris.core.ComponentsProvider
import ws.osiris.core.api

/** The API. */
val api = api<ComponentsProvider>(cors = true) {

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
