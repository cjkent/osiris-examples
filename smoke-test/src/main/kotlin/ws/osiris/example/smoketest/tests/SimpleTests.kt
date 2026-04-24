package ws.osiris.example.smoketest.tests

import ws.osiris.example.smoketest.HttpClient

fun customAuth1(client: HttpClient) {
    val response = client.get("/helloworld")
    check(response.code == 200) { "GET /helloworld: expected 200, got ${response.code}" }
    check(response.body?.contains("hello, world!") == true) { "GET /helloworld: unexpected body: ${response.body}" }

    // /private requires auth — should get 401 or 403 without a token
    val privateResponse = client.get("/private")
    check(privateResponse.code in listOf(401, 403)) {
        "GET /private without auth: expected 401 or 403, got ${privateResponse.code}"
    }
    println("  custom-auth1: PASS")
}

fun customAuth2(client: HttpClient) {
    val response = client.get("/helloworld")
    check(response.code == 200) { "GET /helloworld: expected 200, got ${response.code}" }
    check(response.body?.contains("hello, world!") == true) { "GET /helloworld: unexpected body: ${response.body}" }

    val privateResponse = client.get("/private")
    check(privateResponse.code in listOf(401, 403)) {
        "GET /private without auth: expected 401 or 403, got ${privateResponse.code}"
    }
    println("  custom-auth2: PASS")
}

fun java(client: HttpClient) {
    val response = client.get("/hello/World")
    check(response.code == 200) { "GET /hello/World: expected 200, got ${response.code}" }
    check(response.body?.contains("hello, World!") == true) { "GET /hello/World: unexpected body: ${response.body}" }
    println("  java: PASS")
}

fun cors(client: HttpClient) {
    // Simple CORS endpoint
    val fooResponse = client.get("/foo")
    check(fooResponse.code == 200) { "GET /foo: expected 200, got ${fooResponse.code}" }
    check(fooResponse.body?.contains("hello, foo!") == true) { "GET /foo: unexpected body: ${fooResponse.body}" }

    // POST with JSON body
    val barResponse = client.post("/bar", """{"name":"World"}""", contentType = "application/json")
    check(barResponse.code == 200) { "POST /bar: expected 200, got ${barResponse.code}" }
    check(barResponse.body?.contains("hello, World!") == true) { "POST /bar: unexpected body: ${barResponse.body}" }

    // Non-CORS endpoint
    val bazResponse = client.get("/baz")
    check(bazResponse.code == 200) { "GET /baz: expected 200, got ${bazResponse.code}" }
    check(bazResponse.body?.contains("hello, baz!") == true) { "GET /baz: unexpected body: ${bazResponse.body}" }
    println("  cors: PASS")
}
