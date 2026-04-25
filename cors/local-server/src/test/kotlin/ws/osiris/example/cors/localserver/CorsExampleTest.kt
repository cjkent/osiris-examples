package ws.osiris.example.cors.localserver

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import ws.osiris.example.cors.core.api
import ws.osiris.example.cors.core.createComponents
import ws.osiris.example.testing.ExampleTestEnvironment
import ws.osiris.example.testing.HttpClient

class CorsExampleTest {

    companion object {
        private lateinit var env: ExampleTestEnvironment
        private lateinit var client: HttpClient

        @BeforeAll
        @JvmStatic
        fun start() {
            env = ExampleTestEnvironment("osiris-cors-example", api, createComponents())
            client = HttpClient(env.baseUrl)
        }

        @AfterAll
        @JvmStatic
        fun stop() {
            env.stop()
        }
    }

    @Test
    fun getFoo() {
        val response = client.get("/foo")
        assertThat(response.code).isEqualTo(200)
        assertThat(response.body).contains("hello, foo!")
    }

    @Test
    fun postBar() {
        val response = client.post("/bar", """{"name":"World"}""", contentType = "application/json")
        assertThat(response.code).isEqualTo(200)
        assertThat(response.body).contains("hello, World!")
    }

    @Test
    fun getBaz() {
        val response = client.get("/baz")
        assertThat(response.code).isEqualTo(200)
        assertThat(response.body).contains("hello, baz!")
    }
}
