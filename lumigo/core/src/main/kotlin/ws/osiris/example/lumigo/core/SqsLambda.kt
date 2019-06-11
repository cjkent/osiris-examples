package ws.osiris.example.lumigo.core

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import com.amazonaws.services.dynamodbv2.model.AttributeValue
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.events.SQSEvent
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.google.gson.Gson
import io.lumigo.handlers.LumigoRequestHandler
import org.slf4j.LoggerFactory
import ws.osiris.core.HttpMethod

private val log = LoggerFactory.getLogger("ws.osiris.example.lumigo.core")

/**
 * Lambda function that receives SQS messages and writes or deletes values from S3.
 *
 * The SQS message body is JSON with this format:
 *
 *     { "id": "...", "action", "..." }
 *
 * The action is `PUT` or `DELETE`.
 *
 * If the action is `PUT` the value is read from DynamoDB using the ID and written to S3 using the ID as the key.
 *
 * If the action is `DELETE` the value is deleted from S3; the ID is the key.
 */
class SqsLambda : LumigoRequestHandler<SQSEvent, Unit>() {

    private val gson: Gson = Gson()
    private val dynamoClient: AmazonDynamoDB = AmazonDynamoDBClientBuilder.defaultClient()
    private val s3Client: AmazonS3 = AmazonS3ClientBuilder.defaultClient()

    override fun doHandleRequest(event: SQSEvent, context: Context) {
        val message = event.records[0]
        val messageBodyMap = gson.fromJson(message.body, Map::class.java)
        val id = messageBodyMap.getValue(ID) as String
        val action = messageBodyMap.getValue(ACTION) as String
        log.info("Received message with ID {}, action {}", id, action)
        val result = dynamoClient.getItem(ITEMS_TABLE, mapOf(ID to AttributeValue(id)))
            val value = result.item.getValue(VALUE).s
        when (HttpMethod.valueOf(action)) {
            HttpMethod.PUT -> putValue(id, value)
            HttpMethod.DELETE -> deleteValue(id)
            else -> throw IllegalArgumentException("Unexpected action $action")
        }
    }

    private fun putValue(id: String, value: String) {
        s3Client.putObject(BUCKET_NAME, id, value)
    }

    private fun deleteValue(id: String) {
        s3Client.deleteObject(BUCKET_NAME, id)
    }
}