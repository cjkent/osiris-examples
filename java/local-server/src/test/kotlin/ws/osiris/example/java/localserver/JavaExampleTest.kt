package ws.osiris.example.java.localserver

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import ws.osiris.example.java.core.api
import ws.osiris.example.java.core.createComponents
import ws.osiris.example.testing.ExampleTestEnvironment
import ws.osiris.example.testing.HttpClient

class JavaExampleTest {

    companion object {
        private lateinit var env: ExampleTestEnvironment
        private lateinit var client: HttpClient

        @BeforeAll
        @JvmStatic
        fun start() {
            env = ExampleTestEnvironment("osiris-java-example", api, createComponents())
            client = HttpClient(env.baseUrl)
        }

        @AfterAll
        @JvmStatic
        fun stop() {
            env.stop()
        }
    }

    @Test
    fun helloReturnsGreeting() {
        val response = client.get("/hello/World")
        assertThat(response.code).isEqualTo(200)
        assertThat(response.body).contains("hello, World!")
    }
}
