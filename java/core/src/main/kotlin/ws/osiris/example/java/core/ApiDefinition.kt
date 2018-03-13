package ws.osiris.example.java.core

import ws.osiris.core.api
import ws.osiris.example.java.components.GreetingFactory
import ws.osiris.example.java.components.JavaComponentsProvider
import ws.osiris.example.java.components.JavaComponentsProviderImpl

/** The API. */
val api = api<JavaComponentsProvider> {
    get("/hello/{name}") { req ->
        val name = req.pathParams["name"]
        val greeting = greetingFactory.createGreeting(name)
        mapOf("message" to greeting)
    }
}

/**
 * Creates the components used by the test API.
 */
fun createComponents(): JavaComponentsProvider = JavaComponentsProviderImpl(GreetingFactory())
