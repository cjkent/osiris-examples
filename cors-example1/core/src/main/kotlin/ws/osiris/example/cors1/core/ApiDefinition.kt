package ws.osiris.example.cors1.core

import com.google.gson.Gson
import ws.osiris.core.ComponentsProvider
import ws.osiris.core.HttpHeaders
import ws.osiris.core.HttpMethod
import ws.osiris.core.api

/** The API. */
val api = api<CorsExample1Components>(cors = true) {

    cors {
        allowMethods = setOf(HttpMethod.GET, HttpMethod.POST)
        allowOrigin = setOf("*")
        allowHeaders = setOf(HttpHeaders.CONTENT_TYPE)
    }

    get("/foo") {
        mapOf("message" to "hello, foo!")
    }

    post("/bar") { req ->
        val json = req.body<String>()
        val bodyMap = gson.fromJson(json, Map::class.java)
        val name = bodyMap["name"]
        mapOf("message" to "hello, $name!")
    }

    get("/baz", cors = false) {
        mapOf("message" to "hello, baz!")
    }
}

/**
 * Creates the components used by the API.
 */
fun createComponents(): CorsExample1Components = CorsExample1ComponentsImpl()

/**
 * Simple components that only provides a [Gson] instance.
 */
interface CorsExample1Components : ComponentsProvider {
    val gson: Gson
}

internal class CorsExample1ComponentsImpl : CorsExample1Components {
    override val gson: Gson = Gson()
}
