package ws.osiris.example.cognito.core

import ws.osiris.core.ComponentsProvider
import ws.osiris.core.api

/** The API. */
val api = api<ComponentsProvider> {

    staticFiles {
        path = "/"
        indexFile = "index.html"
    }

    get("/helloworld") {
        // return a map that is automatically converted to JSON
        mapOf("message" to "hello, world!")
    }
}

/**
 * Creates the components used by the example API.
 */
fun createComponents(): ComponentsProvider = object : ComponentsProvider {}
