package ws.osiris.example.resources.localserver

import ws.osiris.example.resources.core.ResourcesExampleComponents
import ws.osiris.example.resources.core.api
import ws.osiris.example.resources.core.config
import ws.osiris.localserver.runLocalServer

fun main(args: Array<String>) {
    // This is copied and pasted from the AWS SQS console.
    // Which means the application must be deployed to AWS and the queue created before the local server can run.
    // It is often easier to develop this way with the code running locally but talking to AWS services.
    // The alternative would be to use the JMS interface for SQS or to wrap SQS in an abstraction that could
    // also be implemented by a local queue.
    // But in this case it's not worth the hassle.
    val sqsQueueUrl = "https://sqs.[TODO - region].amazonaws.com/[TODO - AWS account number]/exampleQueue.fifo"
    val components = ResourcesExampleComponents(sqsQueueUrl)
    runLocalServer(api, components, config, staticFilesDir = "core/src/main/static")
}
