package ws.osiris.example.dynamodb.localserver

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.EnabledIfSystemProperty
import ws.osiris.example.dynamodb.core.api
import ws.osiris.example.dynamodb.core.createComponents
import ws.osiris.example.testing.ExampleTestEnvironment
import ws.osiris.example.testing.HttpClient

/**
 * Integration tests for the DynamoDB example.
 *
 * These tests require a real DynamoDB table and only run when targeting AWS:
 *   mvn test -Dtest.aws=true
 *   mvn test -Dtest.base.url=https://...
 */
@EnabledIfSystemProperty(named = "test.aws", matches = "true", disabledReason = "Requires deployed AWS resources")
class DynamoDbExampleTest {

    companion object {
        private lateinit var env: ExampleTestEnvironment
        private lateinit var client: HttpClient

        @BeforeAll
        @JvmStatic
        fun start() {
            env = ExampleTestEnvironment("osiris-dynamodb-example", api, createComponents())
            client = HttpClient(env.baseUrl)
        }

        @AfterAll
        @JvmStatic
        fun stop() {
            env.stop()
        }
    }

    @Test
    fun crudCycle() {
        val postResponse = client.post("/values", "smoke-test-value")
        assertThat(postResponse.code).isEqualTo(201)
        val location = postResponse.headers["Location"]
        assertThat(location).isNotNull()

        val getResponse = client.get(location!!)
        assertThat(getResponse.code).isEqualTo(200)
        assertThat(getResponse.body).contains("smoke-test-value")

        val listResponse = client.get("/values")
        assertThat(listResponse.code).isEqualTo(200)

        val deleteResponse = client.delete(location)
        assertThat(deleteResponse.code).isEqualTo(204)

        val goneResponse = client.get(location)
        assertThat(goneResponse.code).isEqualTo(404)
    }
}
