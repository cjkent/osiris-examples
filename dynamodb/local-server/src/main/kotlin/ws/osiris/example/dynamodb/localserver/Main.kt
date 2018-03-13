package ws.osiris.example.dynamodb.localserver

import ws.osiris.example.dynamodb.core.api
import ws.osiris.example.dynamodb.core.config
import ws.osiris.example.dynamodb.core.createComponents
import ws.osiris.localserver.runLocalServer

fun main(args: Array<String>) {
    val components = createComponents()
    runLocalServer(api, components, config, staticFilesDir = "core/src/main/static")
}
