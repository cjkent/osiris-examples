package ws.osiris.example.customauth2.core

import ws.osiris.aws.CustomAuth
import ws.osiris.core.ComponentsProvider
import ws.osiris.core.api

/** The API. */
val api = api<ComponentsProvider> {
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
