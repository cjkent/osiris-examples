package ws.osiris.example.lumigo.core

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import com.amazonaws.services.dynamodbv2.model.AttributeValue
import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.AmazonSQSClientBuilder
import com.google.gson.Gson
import io.lumigo.handlers.LumigoConfiguration
import io.lumigo.handlers.LumigoRequestExecutor
import ws.osiris.aws.lambdaContext
import ws.osiris.aws.lambdaEvent
import ws.osiris.core.ComponentsProvider
import ws.osiris.core.HttpHeaders
import ws.osiris.core.HttpMethod
import ws.osiris.core.api
import java.util.UUID

internal const val ITEMS_TABLE: String = "LumigoExampleItems"
internal const val ID: String = "id"
internal const val VALUE: String = "value"
internal const val ACTION: String = "action"

/** The bucket name is a parameter in `root.template` that is passed to the generated CloudFormation file. */
internal val BUCKET_NAME = System.getenv("BucketName")

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
        val messageBody = MessageBody(id, HttpMethod.PUT)
        val messageBodyJson = gson.toJson(messageBody)
        sqsClient.sendMessage(QUEUE_URL, messageBodyJson)
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
        val messageBodyMap = mapOf(ID to id, ACTION to HttpMethod.DELETE.name)
        val messageBody = gson.toJson(messageBodyMap)
        sqsClient.sendMessage(QUEUE_URL, messageBody)
        req.responseBuilder().status(204).build()
    }

    post("/cleanup") {
        // TODO read all the values from dynamo, send DELETE messages, then delete from dynamo
    }
}

private fun item(item: MutableMap<String, AttributeValue>) =
    mapOf(ID to item.getValue(ID).s, VALUE to item.getValue(VALUE).s)

/**
 * Creates the components used by the test API.
 */
fun createComponents(): LumigoExampleComponents {
    LumigoConfiguration.builder().lazyLoading(false).token("TODO your Lumigo token").build().init();
    return LumigoExampleComponentsImpl()
}

/**
 * Components used in the DynamoDB example; contains a DynamoDB client.
 */
interface LumigoExampleComponents : ComponentsProvider {
    val dynamoClient: AmazonDynamoDB
    val sqsClient: AmazonSQS
    val gson: Gson
}

/**
 * Components used in the DynamoDB example; contains a default DynamoDB client.
 */
class LumigoExampleComponentsImpl : LumigoExampleComponents {
    override val dynamoClient: AmazonDynamoDB = AmazonDynamoDBClientBuilder.defaultClient()
    override val sqsClient: AmazonSQS = AmazonSQSClientBuilder.defaultClient()
    override val gson: Gson = Gson()
}

data class MessageBody(val id: String, val action: HttpMethod)
