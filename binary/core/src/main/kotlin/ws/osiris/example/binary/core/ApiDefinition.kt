package ws.osiris.example.binary.core

import org.imgscalr.Scalr
import ws.osiris.core.ComponentsProvider
import ws.osiris.core.ContentType
import ws.osiris.core.HttpHeaders
import ws.osiris.core.api
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

/** The API. */
val api = api<ComponentsProvider> {

    // Rotates an image clockwise by 90 degrees
    // The Content-Type header of the request must be image/png or image/jpeg
    // The Accept header must be the same as the Content-Type header
    // The body must be a binary image whose format matches the content type
    post("/rotate90") { req ->
        val contentTypeHeader = req.headers[HttpHeaders.CONTENT_TYPE]
        val (mimeType, _) = ContentType.parse(contentTypeHeader)
        if (!config.binaryMimeTypes.contains(mimeType)) {
            throw IllegalArgumentException("Content-Type must be one of ${config.binaryMimeTypes}")
        }
        val image = ImageIO.read(ByteArrayInputStream(req.requireBinaryBody()))
        val rotatedImage = Scalr.rotate(image, Scalr.Rotation.CW_90)
        val formatName = mimeType.substring(mimeType.indexOf('/') + 1)
        val outputStream = ByteArrayOutputStream()
        ImageIO.write(rotatedImage, formatName, outputStream)
        val byteArray = outputStream.toByteArray()
        req.responseBuilder()
            .header(HttpHeaders.CONTENT_TYPE, contentTypeHeader)
            .header(HttpHeaders.CONTENT_LENGTH, byteArray.size.toString())
            .build(byteArray)
    }
}

/**
 * Creates the components used by the test API.
 */
fun createComponents(): ComponentsProvider = object : ComponentsProvider {}
