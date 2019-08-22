package ws.osiris.example.thundra.localserver

import ws.osiris.example.thundra.core.api
import ws.osiris.example.thundra.core.createComponents
import ws.osiris.localserver.runLocalServer

fun main() {
    val components = createComponents()
    runLocalServer(api, components, staticFilesDir = "core/src/main/static")
}
