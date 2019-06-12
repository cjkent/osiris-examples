package ws.osiris.example.lumigo.core

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import com.amazonaws.services.dynamodbv2.model.AttributeValue
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.SQSEvent
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.google.gson.Gson
import io.lumigo.handlers.LumigoConfiguration
import io.lumigo.handlers.LumigoRequestExecutor
import org.slf4j.LoggerFactory
import ws.osiris.core.HttpMethod

internal val log = LoggerFactory.getLogger("ws.osiris.example.lumigo.core")

/** The bucket name is a parameter in `root.template` that is passed to the generated CloudFormation file. */
private val BUCKET_NAME = System.getenv("BucketName")

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
class SqsLambda : RequestHandler<SQSEvent, Unit> {

    init {
        LumigoConfiguration.builder().token(LUMIGO_TOKEN).build().init();
    }

    private val gson: Gson = Gson()
    private val dynamoClient: AmazonDynamoDB = AmazonDynamoDBClientBuilder.defaultClient()
    private val s3Client: AmazonS3 = AmazonS3ClientBuilder.defaultClient()

    override fun handleRequest(event: SQSEvent, context: Context) {
        LumigoRequestExecutor.execute(event, context) {
            val message = event.records[0]
            val messageBody = gson.fromJson(message.body, MessageBody::class.java)
            log.info("Received message with ID {}, action {}", messageBody.id, messageBody.action)
            val result = dynamoClient.getItem(ITEMS_TABLE, mapOf(ID to AttributeValue(messageBody.id)))
            val value = result.item.getValue(VALUE).s
            when (messageBody.action) {
                HttpMethod.PUT -> putValue(messageBody.id, value)
                HttpMethod.DELETE -> deleteValue(messageBody.id)
                else -> throw IllegalArgumentException("Unexpected action ${messageBody.action}")
            }
        }
    }

    private fun putValue(id: String, value: String) {
        log.debug("Writing to S3 at {}/{}, value '{}'", BUCKET_NAME, id, value)
        s3Client.putObject(BUCKET_NAME, id, value)
    }

    private fun deleteValue(id: String) {
        log.debug("Deleting object from S3, {}/{}", BUCKET_NAME, id)
        s3Client.deleteObject(BUCKET_NAME, id)
    }
}
