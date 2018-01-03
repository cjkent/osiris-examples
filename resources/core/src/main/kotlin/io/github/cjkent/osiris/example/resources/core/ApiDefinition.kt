package io.github.cjkent.osiris.example.resources.core

import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.AmazonSQSClientBuilder
import com.amazonaws.services.sqs.model.ReceiveMessageRequest
import com.amazonaws.services.sqs.model.SendMessageRequest
import io.github.cjkent.osiris.core.ComponentsProvider
import io.github.cjkent.osiris.core.api

/** The API. */
val api = api<ResourcesExampleComponents> {

    post("/messages") { req ->
        val body = req.requireBody(String::class)
        val result = sqsClient.sendMessage(SendMessageRequest().apply {
            queueUrl = sqsQueueUrl
            messageBody = body
            messageGroupId = "exampleGroup"
        })
        mapOf("messageId" to result.messageId)
    }

    get("/messages") { req ->
        val messageCount = req.queryParams["count"].toInt()
        val result = sqsClient.receiveMessage(ReceiveMessageRequest().apply {
            queueUrl = sqsQueueUrl
            maxNumberOfMessages = messageCount
        })
        val messageMaps = result.messages.map {
            mapOf(
                "messageId" to it.messageId,
                "receiptHandle" to it.receiptHandle,
                "body" to it.body
            )
        }
        mapOf("messages" to messageMaps)
    }

    // Using POST to delete seems weird but SQS requires the receipt handle to delete a message.
    // This doesn't work with standard REST deletion, where the entity ID would be used in the path.
    // Something like would be more standard, but the message ID isn't any use
    //     delete("/messages/{messageId}") { ...
    // Also, using the receipt handle in the path wouldn't work as it's an enormous string that's full
    // of characters that can't be used in a URL
    post("/messages/delete") { req ->
        sqsClient.deleteMessage(sqsQueueUrl, req.requireBody(String::class))
    }
}

/**
 * Creates the components used by the test API.
 */
fun createComponents(): ResourcesExampleComponents = ResourcesExampleComponents()

/**
 * Components containing an SQS client and the URL of an SQS queue.
 */
class ResourcesExampleComponents(val sqsQueueUrl: String) : ComponentsProvider {

    /** Constructor that takes the URL of the queue from an environment variable `QueueUrl`. */
    constructor() : this(System.getenv("QueueUrl") ?: throw IllegalStateException("No env var QueueUrl defined"))

    val sqsClient: AmazonSQS = AmazonSQSClientBuilder.defaultClient()
}
