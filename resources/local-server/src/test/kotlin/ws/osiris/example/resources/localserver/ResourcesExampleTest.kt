package ws.osiris.example.resources.localserver

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.EnabledIfSystemProperty
import ws.osiris.example.resources.core.api
import ws.osiris.example.resources.core.createComponents
import ws.osiris.example.testing.ExampleTestEnvironment
import ws.osiris.example.testing.HttpClient

/**
 * Integration tests for the Resources (SQS) example.
 *
 * These tests require a real SQS queue and only run when targeting AWS:
 *   mvn test -Dtest.aws=true
 *   mvn test -Dtest.base.url=https://...
 */
@EnabledIfSystemProperty(named = "test.aws", matches = "true", disabledReason = "Requires deployed AWS resources")
class ResourcesExampleTest {

    companion object {
        private lateinit var env: ExampleTestEnvironment
        private lateinit var client: HttpClient

        @BeforeAll
        @JvmStatic
        fun start() {
            env = ExampleTestEnvironment("osiris-resources-example", api, createComponents())
            client = HttpClient(env.baseUrl)
        }

        @AfterAll
        @JvmStatic
        fun stop() {
            env.stop()
        }
    }

    @Test
    fun postAndGetMessages() {
        val postResponse = client.post("/messages", "smoke-test-message")
        assertThat(postResponse.code).isEqualTo(200)
        assertThat(postResponse.body).contains("messageId")

        val getResponse = client.get("/messages?count=1")
        assertThat(getResponse.code).isEqualTo(200)
    }
}
