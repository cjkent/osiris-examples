package ws.osiris.example.customauth1.localserver

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import ws.osiris.example.customauth1.core.api
import ws.osiris.example.customauth1.core.createComponents
import ws.osiris.example.testing.ExampleTestEnvironment
import ws.osiris.example.testing.HttpClient

class CustomAuth1ExampleTest {

    companion object {
        private lateinit var env: ExampleTestEnvironment
        private lateinit var client: HttpClient

        @BeforeAll
        @JvmStatic
        fun start() {
            env = ExampleTestEnvironment("osiris-custom-auth-example1", api, createComponents())
            client = HttpClient(env.baseUrl)
        }

        @AfterAll
        @JvmStatic
        fun stop() {
            env.stop()
        }
    }

    @Test
    fun helloWorldReturnsGreeting() {
        val response = client.get("/helloworld")
        assertThat(response.code).isEqualTo(200)
        assertThat(response.body).contains("hello, world!")
    }
}
