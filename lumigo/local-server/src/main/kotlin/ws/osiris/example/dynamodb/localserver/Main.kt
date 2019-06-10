package ws.osiris.example.lumigo.localserver

import ws.osiris.example.lumigo.core.api
import ws.osiris.example.lumigo.core.createComponents
import ws.osiris.localserver.runLocalServer

fun main() {
    val components = createComponents()
    runLocalServer(api, components, staticFilesDir = "core/src/main/static")
}
