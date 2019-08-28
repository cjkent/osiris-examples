package ws.osiris.example.thundra.core

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import com.amazonaws.services.dynamodbv2.model.AttributeValue
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.SQSEvent
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.google.gson.Gson
import ws.osiris.core.HttpMethod

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

    private val gson: Gson = Gson()
    private val dynamoClient: AmazonDynamoDB = AmazonDynamoDBClientBuilder.defaultClient()
    private val s3Client: AmazonS3 = AmazonS3ClientBuilder.defaultClient()

    override fun handleRequest(event: SQSEvent, context: Context) {
        val message = event.records[0]
        val messageBody = gson.fromJson(message.body, MessageBody::class.java)
        context.logger.log(String.format("Received message with ID %s, action %s", messageBody.id, messageBody.action))
        when (messageBody.action) {
            HttpMethod.PUT -> putValue(messageBody.id, context)
            HttpMethod.DELETE -> deleteValue(messageBody.id, context)
            else -> throw IllegalArgumentException("Unexpected action ${messageBody.action}")
        }
    }

    private fun putValue(id: String, context: Context) {
        val result = dynamoClient.getItem(ITEMS_TABLE, mapOf(ID to AttributeValue(id)))
        val value = result.item.getValue(VALUE).s
        context.logger.log(String.format("Writing object to S3 at %s/%s, value '%s'", BUCKET_NAME, id, value))
        s3Client.putObject(BUCKET_NAME, id, value)
    }

    private fun deleteValue(id: String, context: Context) {
        context.logger.log(String.format("Deleting object from S3, %s/%s", BUCKET_NAME, id))
        s3Client.deleteObject(BUCKET_NAME, id)
    }

}
