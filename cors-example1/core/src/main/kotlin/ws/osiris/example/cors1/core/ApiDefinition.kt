package ws.osiris.example.cors1.core

import com.google.gson.Gson
import ws.osiris.core.ComponentsProvider
import ws.osiris.core.HttpHeaders
import ws.osiris.core.HttpMethod
import ws.osiris.core.api

/**
 * An API that allows Cross-Origin Resource Sharing (CORS).
 *
 * See here for a detailed explanation of CORS:
 *
 *     https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS
 *
 * This allows a browser script to make a request to an origin that is not the same as the origin
 * from which its page was downloaded.
 *
 * For example, a script in a page downloaded from `www.example.com` would not normally be allowed
 * to make a request to `api.example.com`. If the API at `api.example.com` uses CORS then it can
 * tell the browser that the script should be allowed to make the request.
 *
 * The `cors` flag in the API is `true` so all endpoints will be CORS-enabled unless the endpoint itself
 * specifies `cors = false`.
 */
val api = api<CorsExample1Components>(cors = true) {

    /**
     * Generates the CORS headers for any CORS-enabled endpoint.
     *
     * The request is passed to the block in case the CORS headers need to be different for different
     * endpoints. In this case the request isn't used and the same headers are returned for all
     * CORS endpoints.
     */
    cors { req ->
        allowMethods = setOf(HttpMethod.POST)
        allowOrigin = setOf("*")
        allowHeaders = setOf(HttpHeaders.CONTENT_TYPE)
    }

    /**
     * A request to this endpoint will be a so-called simple CORS request.
     *
     * The response to a simple CORS request must include the header `Access-Control-Allow-Origin`
     * with a value including the origin of the calling script.
     *
     * This is set by setting the value of the `allowOrigin` property in the `cors` block.
     */
    get("/foo") {
        mapOf("message" to "hello, foo!")
    }

    /**
     * This endpoint will be called with a `Content-Type` header of `application/json`; this means it
     * it a so-called non-simple request.
     *
     * The browser will make a "pre-flight" request to the OPTIONS method for this path. The response
     * must contain the headers:
     *
     *   * `Access-Control-Allow-Origin`
     *   * `Access-Control-Allow-Methods`
     *   * `Access-Control-Allow-Headers`
     *
     * These are specified by setting the properties in the `cors` block.
     */
    post("/bar") { req ->
        val json = req.body<String>()
        val bodyMap = gson.fromJson(json, Map::class.java)
        val name = bodyMap["name"]
        mapOf("message" to "hello, $name!")
    }

    /**
     * This endpoint is not CORS-enabled so it cannot be called from a script in a page that was not
     * loaded from the same origin as the API.
     */
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
