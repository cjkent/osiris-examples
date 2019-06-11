package ws.osiris.example.lumigo.core

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import com.amazonaws.services.dynamodbv2.model.AttributeValue
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.AmazonSQSClientBuilder
import io.lumigo.handlers.LumigoRequestExecutor
import ws.osiris.aws.lambdaContext
import ws.osiris.aws.lambdaEvent
import ws.osiris.core.ComponentsProvider
import ws.osiris.core.HttpHeaders
import ws.osiris.core.api
import java.util.UUID

private const val ITEMS_TABLE: String = "Items"
private const val ID: String = "id"
private const val VALUE: String = "value"

/** The bucket name is a parameter in `root.template` that is passed to the generated CloudFormation file. */
private val BUCKET_NAME = System.getenv("BucketName")

/** The queue URL is a parameter in `root.template` that is passed to the generated CloudFormation file. */
private val QUEUE_URL = System.getenv("QueueUrl")

/** The API. */
val api = api<LumigoExampleComponents> {

    filter { req, handler ->
        LumigoRequestExecutor.execute(req.lambdaEvent, req.lambdaContext) {
            handler(req)
        }
    }

    get("/values") {
        val items = dynamoClient.scan(ITEMS_TABLE, listOf(ID, VALUE)).items
        items.map { item(it) }
    }
    post("/values") { req ->
        val id = UUID.randomUUID().toString()
        val value = req.requireBody(String::class)
        dynamoClient.putItem(ITEMS_TABLE, mapOf(ID to AttributeValue(id), VALUE to AttributeValue(value)))
        req.responseBuilder().status(201).header(HttpHeaders.LOCATION, "/values/$id").build()
    }
    get("/values/{id}") { req ->
        val id = req.pathParams[ID]
        val item = dynamoClient.getItem(ITEMS_TABLE, mapOf(ID to AttributeValue(id))).item
        if (item == null) {
            req.responseBuilder().status(404).build()
        } else {
            item(item)
        }
    }
    delete("/values/{id}") { req ->
        val id = req.pathParams[ID]
        dynamoClient.deleteItem(ITEMS_TABLE, mapOf(ID to AttributeValue(id)))
        req.responseBuilder().status(204).build()
    }
}

private fun item(item: MutableMap<String, AttributeValue>) =
    mapOf(ID to item.getValue(ID).s, VALUE to item.getValue(VALUE).s)

/**
 * Creates the components used by the test API.
 */
fun createComponents(): LumigoExampleComponents = LumigoExampleComponentsImpl()

/**
 * Components used in the DynamoDB example; contains a DynamoDB client.
 */
interface LumigoExampleComponents : ComponentsProvider {
    val dynamoClient: AmazonDynamoDB
    val s3Client: AmazonS3
    val sqsClient: AmazonSQS
}

/**
 * Components used in the DynamoDB example; contains a default DynamoDB client.
 */
class LumigoExampleComponentsImpl : LumigoExampleComponents {
    override val dynamoClient: AmazonDynamoDB = AmazonDynamoDBClientBuilder.defaultClient()
    override val s3Client: AmazonS3 = AmazonS3ClientBuilder.defaultClient()
    override val sqsClient: AmazonSQS = AmazonSQSClientBuilder.defaultClient()
}

// TODO a stand-alone lambda function that receives values over SQS and writes them to S3
//   * ID is sent over SQS, lambda reads the value from Dynamo and stores in S3
//   * SQS message includes an action "PUT" or "DELETE"
// TODO a /cleanup endpoint that deletes everything from dynamo, S3 and SQS. so the stack can be deleted
