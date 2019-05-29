package ws.osiris.example.customauth1.localserver

import ws.osiris.example.customauth1.core.api
import ws.osiris.example.customauth1.core.createComponents
import ws.osiris.localserver.runLocalServer

fun main() {
    val components = createComponents()
    runLocalServer(api, components, staticFilesDir = "core/src/main/static")
}
