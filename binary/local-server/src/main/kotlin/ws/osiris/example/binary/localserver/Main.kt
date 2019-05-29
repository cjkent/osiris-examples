package ws.osiris.example.binary.localserver

import ws.osiris.example.binary.core.api
import ws.osiris.example.binary.core.createComponents
import ws.osiris.localserver.runLocalServer

fun main() {
    val api = api
    val components = createComponents()
    runLocalServer(api, components, staticFilesDir = "core/src/main/static")
}
