package ws.osiris.example.binary.localserver

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import ws.osiris.example.binary.core.api
import ws.osiris.example.binary.core.createComponents
import ws.osiris.example.testing.ExampleTestEnvironment
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

class BinaryExampleTest {

    companion object {
        private lateinit var env: ExampleTestEnvironment
        private lateinit var baseUrl: String

        @BeforeAll
        @JvmStatic
        fun start() {
            env = ExampleTestEnvironment("osiris-binary-example", api, createComponents())
            baseUrl = env.baseUrl
        }

        @AfterAll
        @JvmStatic
        fun stop() {
            env.stop()
        }
    }

    @Test
    fun rotatePng() {
        val png = createTestPng()
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("$baseUrl/rotate90")
            .post(png.toRequestBody("image/png".toMediaType()))
            .header("Accept", "image/png")
            .build()
        client.newCall(request).execute().use { response ->
            assertThat(response.code).isEqualTo(200)
            assertThat(response.body).isNotNull()
        }
    }

    private fun createTestPng(): ByteArray {
        val image = BufferedImage(10, 20, BufferedImage.TYPE_INT_RGB)
        val output = ByteArrayOutputStream()
        ImageIO.write(image, "png", output)
        return output.toByteArray()
    }
}
