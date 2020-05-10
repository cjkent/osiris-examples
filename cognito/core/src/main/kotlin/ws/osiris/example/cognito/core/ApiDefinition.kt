package ws.osiris.example.cognito.core

import com.google.gson.Gson
import ws.osiris.aws.CognitoUserPoolsAuth
import ws.osiris.core.ComponentsProvider
import ws.osiris.core.HttpHeaders
import ws.osiris.core.MimeTypes
import ws.osiris.core.api
import java.util.Base64

/** The API. */
val api = api<CognitoExampleComponentsProvider> {

    staticFiles {
        path = "/"
        indexFile = "index.html"
    }

    // filter to decode the Authorization header JWT and get out the email and cognito:username fields
    filter { req, handler ->
        // all endpoints in the API require Cognito auth so it's safe to assume the header will be here
        val authHeader = req.headers[HttpHeaders.AUTHORIZATION]
        // the token is 3 base64 strings delimited by dots, the second token is the one that contains the user data
        val (_, tokenBase64, _) = authHeader.split(".")
        val token = String(Base64.getDecoder().decode(tokenBase64))
        @Suppress("UNCHECKED_CAST")
        // the token is JSON - convert to a map
        val tokenMap = gson.fromJson(token, Map::class.java) as Map<Any, Any>
        val email = tokenMap.getValue("email")
        val cognitoUsername = tokenMap.getValue("cognito:username")
        // put the user info into request attributes so it can be used by the endpoints
        val updatedReq = req.withAttribute("email", email).withAttribute("cognitoUsername", cognitoUsername)
        // pass the updated request containing the user info on to the next handler (the endpoint)
        handler(updatedReq)
    }

    auth(CognitoUserPoolsAuth) {

        get("/hello") { req ->
            // get the user info from the request attributes that were populated by the filter
            val email = req.attribute<String>("email")
            val cognitoUsername = req.attribute<String>("cognitoUsername")
            req.responseBuilder()
                .header(HttpHeaders.CONTENT_TYPE, MimeTypes.TEXT_PLAIN)
                .build("hello, $email! your Cognito username is '$cognitoUsername'")
        }
    }
}

/**
 * Creates the components used by the example API.
 */
fun createComponents(): CognitoExampleComponentsProvider = CognitoExampleComponentsProviderImpl()

interface CognitoExampleComponentsProvider : ComponentsProvider {
    val gson: Gson
}

class CognitoExampleComponentsProviderImpl : CognitoExampleComponentsProvider {
    override val gson: Gson = Gson()
}
