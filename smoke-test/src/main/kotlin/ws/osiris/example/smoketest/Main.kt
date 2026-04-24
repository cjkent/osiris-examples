package ws.osiris.example.smoketest

import com.beust.jcommander.JCommander
import com.beust.jcommander.Parameter
import ws.osiris.example.smoketest.tests.*

private val testsByName: Map<String, (HttpClient) -> Unit> = mapOf(
    "custom-auth1" to ::customAuth1,
    "custom-auth2" to ::customAuth2,
    "java" to ::java,
    "cors" to ::cors,
    "dynamodb" to ::dynamodb,
    "resources" to ::resources,
    "binary" to ::binary,
)

private val appNames: Map<String, String> = mapOf(
    "custom-auth1" to "osiris-custom-auth-example1",
    "custom-auth2" to "osiris-custom-auth-example2",
    "java" to "osiris-java-example",
    "cors" to "osiris-cors-example",
    "dynamodb" to "osiris-dynamodb-example",
    "resources" to "osiris-resources-example",
    "binary" to "osiris-binary-example",
)

class Args {
    @Parameter(names = ["--local"], description = "Run against a local server")
    var local: Boolean = false

    @Parameter(names = ["--aws"], description = "Run against deployed API Gateway endpoints")
    var aws: Boolean = false

    @Parameter(names = ["--url"], description = "Run against an explicit base URL")
    var url: String? = null

    @Parameter(names = ["--port", "-p"], description = "Local server port (default 8080)")
    var port: Int = 8080

    @Parameter(names = ["--stage", "-s"], description = "API Gateway stage name (default dev)")
    var stage: String = "dev"

    @Parameter(names = ["--test", "-t"], description = "Run a single test by name")
    var test: String? = null

    @Parameter(names = ["--help", "-h"], help = true, description = "Show usage")
    var help: Boolean = false
}

fun main(args: Array<String>) {
    val parsedArgs = Args()
    val jc = JCommander.newBuilder().addObject(parsedArgs).build()
    jc.programName = "smoke-test"
    jc.parse(*args)

    if (parsedArgs.help) {
        jc.usage()
        println("\nAvailable tests: ${testsByName.keys.joinToString(", ")}")
        return
    }

    val testsToRun = parsedArgs.test?.let { name ->
        val test = testsByName[name] ?: error("Unknown test '$name'. Available: ${testsByName.keys}")
        mapOf(name to test)
    } ?: testsByName

    when {
        parsedArgs.local -> runTests(LocalResolver(parsedArgs.port).resolve(), testsToRun)
        parsedArgs.url != null -> runTests(parsedArgs.url!!, testsToRun)
        parsedArgs.aws -> runTestsAgainstAws(parsedArgs.stage, testsToRun)
        else -> jc.usage()
    }
}

private fun runTestsAgainstAws(stage: String, tests: Map<String, (HttpClient) -> Unit>) {
    tests.forEach { (name, test) ->
        val appName = appNames[name] ?: error("No application name for test '$name'")
        val baseUrl = ApiGatewayResolver(appName, stage).resolve()
        println("Running $name against $baseUrl")
        runTest(name, baseUrl, test)
    }
}

private fun runTests(baseUrl: String, tests: Map<String, (HttpClient) -> Unit>) {
    println("Running against $baseUrl")
    tests.forEach { (name, test) -> runTest(name, baseUrl, test) }
}

private fun runTest(name: String, baseUrl: String, test: (HttpClient) -> Unit) {
    val client = HttpClient(baseUrl)
    try {
        test(client)
    } catch (e: Exception) {
        println("  $name: FAIL - ${e.message}")
    }
}
