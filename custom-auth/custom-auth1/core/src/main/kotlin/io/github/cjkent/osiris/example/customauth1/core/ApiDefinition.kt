package io.github.cjkent.osiris.example.customauth1.core

import io.github.cjkent.osiris.aws.CustomAuth
import io.github.cjkent.osiris.core.ComponentsProvider
import io.github.cjkent.osiris.core.api
import org.slf4j.LoggerFactory

/** The API. */
val api = api<ComponentsProvider> {
    get("/helloworld") {
        mapOf("message" to "hello, world!")
    }
    auth(CustomAuth) {
        get("/private") {
            mapOf("message" to "hello, authorised user!")
        }
    }
}

/**
 * Creates the components used by the test API.
 */
fun createComponents(): ComponentsProvider = object : ComponentsProvider {}

//--------------------------------------------------------------------------------------------------

/**
 * A lambda function that is used as a custom authoriser by API Gateway.
 *
 * It is deployed separately from the API by adding hand-written resource to the file `root.template`.
 *
 * When a request is made to an endpoint with custom authorisation then this lambda is invoked.
 * If the caller is authorised to invoke the endpoint then this lambda returns an IAM policy document
 * with permission to invoke the endpoint.
 */
class CustomAuthorizer {

    fun authorize(event: Event): Map<String, Any> {
        // If the auth token is the string 'allow' then authorise the user.
        // A real authorisation lambda would validate the token before authorising the user
        if (event.authorizationToken != "allow") {
            log.info("Refusing authorization. Token = {}", event.authorizationToken)
            throw RuntimeException("Unauthorized")
        }
        log.info("Authorizing. Token = {}", event.authorizationToken)
        // If the user is authorised a custom auth lambda must return an IAM policy document that allows
        // the caller to invoke the endpoint and method
        return mapOf(
            "principalId" to "user",
            "policyDocument" to policyDocument(event.methodArn))
    }

    private fun policyDocument(methodArn: String): Map<String, Any> = mapOf(
        "Version" to "2012-10-17",
        "Statement" to listOf(
            mapOf(
                "Action" to "execute-api:Invoke",
                "Effect" to "Allow",
                "Resource" to methodArn
            )
        )
    )

    companion object {
        private val log = LoggerFactory.getLogger(CustomAuthorizer::class.java)
    }
}

/**
 * The event passed into the authorisation lambda function by API Gateway.
 */
data class Event(var methodArn: String = "", var authorizationToken: String? = null)
