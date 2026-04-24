package ws.osiris.example.smoketest

import com.amazonaws.services.apigateway.AmazonApiGateway
import com.amazonaws.services.apigateway.AmazonApiGatewayClientBuilder
import com.amazonaws.services.apigateway.model.GetRestApisRequest
import com.amazonaws.services.apigateway.model.RestApi
import com.amazonaws.regions.DefaultAwsRegionProviderChain

/**
 * Resolves the base URL for an osiris application.
 */
interface BaseUrlResolver {
    fun resolve(): String
}

/**
 * Returns a local server URL.
 */
class LocalResolver(private val port: Int) : BaseUrlResolver {
    override fun resolve(): String = "http://localhost:$port"
}

/**
 * Looks up the API Gateway REST API by name and constructs the stage URL.
 *
 * This uses the same approach as the osiris maven plugin's `open` goal.
 */
class ApiGatewayResolver(
    private val applicationName: String,
    private val stage: String,
) : BaseUrlResolver {

    override fun resolve(): String {
        val client = AmazonApiGatewayClientBuilder.defaultClient()
        val apis = fetchAllApis(client)
        val api = apis.find { it.name == applicationName }
            ?: error("No API Gateway REST API found with name '$applicationName'")
        val region = DefaultAwsRegionProviderChain().region
        return "https://${api.id}.execute-api.$region.amazonaws.com/$stage"
    }

    private fun fetchAllApis(
        client: AmazonApiGateway,
        position: String? = null,
    ): List<RestApi> {
        val result = client.getRestApis(GetRestApisRequest().withLimit(500).apply {
            if (position != null) this.position = position
        })
        val apis = result.items.orEmpty()
        return if (result.position != null) apis + fetchAllApis(client, result.position) else apis
    }
}
