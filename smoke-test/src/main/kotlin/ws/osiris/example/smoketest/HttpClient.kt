package ws.osiris.example.smoketest

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

/**
 * Simple HTTP response.
 */
data class HttpResponse(
    val code: Int,
    val body: String?,
    val headers: Map<String, String>,
)

/**
 * Thin HTTP client wrapper around OkHttp.
 */
class HttpClient(private val baseUrl: String) {

    private val client = OkHttpClient()

    fun get(path: String, headers: Map<String, String> = emptyMap()): HttpResponse =
        execute(Request.Builder().url("$baseUrl$path").apply {
            headers.forEach { (k, v) -> addHeader(k, v) }
        }.build())

    fun post(path: String, body: String, contentType: String = "text/plain", headers: Map<String, String> = emptyMap()): HttpResponse {
        val mediaType = contentType.toMediaType()
        return execute(Request.Builder().url("$baseUrl$path").post(body.toRequestBody(mediaType)).apply {
            headers.forEach { (k, v) -> addHeader(k, v) }
        }.build())
    }

    fun delete(path: String, headers: Map<String, String> = emptyMap()): HttpResponse =
        execute(Request.Builder().url("$baseUrl$path").delete().apply {
            headers.forEach { (k, v) -> addHeader(k, v) }
        }.build())

    private fun execute(request: Request): HttpResponse {
        client.newCall(request).execute().use { response ->
            val headerMap = response.headers.names().associateWith { response.header(it)!! }
            return HttpResponse(response.code, response.body?.string(), headerMap)
        }
    }
}
