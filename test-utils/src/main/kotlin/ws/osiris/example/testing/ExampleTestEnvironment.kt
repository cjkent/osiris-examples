package ws.osiris.example.testing

import com.amazonaws.regions.DefaultAwsRegionProviderChain
import com.amazonaws.services.apigateway.AmazonApiGatewayClientBuilder
import com.amazonaws.services.apigateway.model.GetRestApisRequest
import com.amazonaws.services.apigateway.model.RestApi
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.ServerConnector
import ws.osiris.core.Api
import ws.osiris.core.ComponentsProvider
import ws.osiris.localserver.createLocalServer

/**
 * Test environment that resolves the base URL for an osiris example application.
 *
 * Three modes, checked in order:
 *
 * 1. `test.base.url` system property — uses the explicit URL directly.
 * 2. `test.aws` system property set to `true` — looks up the API Gateway REST API
 *    by [applicationName] and constructs the stage URL. The stage defaults to `dev`
 *    and can be overridden with `test.aws.stage`.
 * 3. Otherwise — starts a local Jetty server on a random port.
 */
class ExampleTestEnvironment(
    private val applicationName: String,
    api: Api<out ComponentsProvider>,
    components: ComponentsProvider,
    staticFilesDir: String? = null,
) {
    val baseUrl: String
    private val server: Server?

    init {
        val explicitUrl = System.getProperty("test.base.url")
        val awsMode = System.getProperty("test.aws")?.toBoolean() == true

        when {
            explicitUrl != null -> {
                baseUrl = explicitUrl.trimEnd('/')
                server = null
            }
            awsMode -> {
                val stage = System.getProperty("test.aws.stage") ?: "dev"
                baseUrl = resolveApiGatewayUrl(applicationName, stage)
                server = null
            }
            else -> {
                @Suppress("UNCHECKED_CAST")
                val typedApi = api as Api<ComponentsProvider>
                val localServer = createLocalServer(typedApi, components, port = 0, staticFilesDir = staticFilesDir)
                localServer.start()
                val port = (localServer.connectors[0] as ServerConnector).localPort
                baseUrl = "http://localhost:$port"
                server = localServer
            }
        }
    }

    fun stop() {
        server?.stop()
    }

    companion object {

        private fun resolveApiGatewayUrl(applicationName: String, stage: String): String {
            val client = AmazonApiGatewayClientBuilder.defaultClient()
            val api = fetchAllApis(client).find { it.name == applicationName }
                ?: error("No API Gateway REST API found with name '$applicationName'")
            val region = DefaultAwsRegionProviderChain().region
            return "https://${api.id}.execute-api.$region.amazonaws.com/$stage"
        }

        private fun fetchAllApis(
            client: com.amazonaws.services.apigateway.AmazonApiGateway,
            position: String? = null,
        ): List<RestApi> {
            val result = client.getRestApis(GetRestApisRequest().withLimit(500).apply {
                if (position != null) this.position = position
            })
            val apis = result.items.orEmpty()
            return if (result.position != null) apis + fetchAllApis(client, result.position) else apis
        }
    }
}
