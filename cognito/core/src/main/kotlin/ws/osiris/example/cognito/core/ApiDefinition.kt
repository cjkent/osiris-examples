package ws.osiris.example.cognito.core

import ws.osiris.core.ComponentsProvider
import ws.osiris.core.HttpHeaders
import ws.osiris.core.MimeTypes
import ws.osiris.core.api

/** The API. */
val api = api<ComponentsProvider> {

    staticFiles {
        path = "/"
        indexFile = "index.html"
    }

    get("/hello") { req ->
        req.responseBuilder()
            .header(HttpHeaders.CONTENT_TYPE, MimeTypes.TEXT_PLAIN)
            .build("hello, world!")
    }
}

/**
 * Creates the components used by the example API.
 */
fun createComponents(): ComponentsProvider = object : ComponentsProvider {}
