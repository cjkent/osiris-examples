package ws.osiris.example.dynamodb.localserver

import ws.osiris.example.dynamodb.core.api
import ws.osiris.example.dynamodb.core.createComponents
import ws.osiris.localserver.runLocalServer

fun main() {
    val components = createComponents()
    runLocalServer(api, components, staticFilesDir = "core/src/main/static")
}
