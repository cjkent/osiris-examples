package io.github.cjkent.osiris.example.java.core

import io.github.cjkent.osiris.core.api
import io.github.cjkent.osiris.example.components.GreetingFactory
import io.github.cjkent.osiris.example.components.JavaComponentsProvider
import io.github.cjkent.osiris.example.components.JavaComponentsProviderImpl

/** The API. */
val api = api(JavaComponentsProvider::class) {
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
