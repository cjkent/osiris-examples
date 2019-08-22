package ws.osiris.example.thundra.core

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.S3Event
import com.amazonaws.services.sns.AmazonSNS
import com.amazonaws.services.sns.AmazonSNSClientBuilder
import com.google.gson.JsonObject

/** The topic ARN is a parameter in `root.template` that is passed to the generated CloudFormation file. */
private val TOPIC_ARN = System.getenv("TopicArn")

/**
 * Lambda function which is notified from S3 and sends notifications to SNS topic.
 */
class S3Lambda : RequestHandler<S3Event, Unit> {

    private val snsClient: AmazonSNS = AmazonSNSClientBuilder.defaultClient()

    override fun handleRequest(event: S3Event, context: Context) {
        for (record in event.records) {
            if (record.eventName.startsWith("ObjectCreated")) {
                context.logger.log("Saved object to S3 with name " + record.s3.`object`.key)
                publishNotification("SAVE", record.s3.`object`.key, context)
            } else if (record.eventName.startsWith("ObjectRemoved")) {
                context.logger.log("Deleted object from S3 with name " + record.s3.`object`.key)
                publishNotification("DELETE", record.s3.`object`.key, context)
            }
        }
    }

    private fun publishNotification(type: String, key: String, context: Context) {
        val notification = JsonObject()
        notification.addProperty("type", type)
        notification.addProperty("key", key)
        context.logger.log(String.format("Publishing notification %s to topic %s", notification.toString(), TOPIC_ARN))
        snsClient.publish(TOPIC_ARN, notification.toString())
    }

}
