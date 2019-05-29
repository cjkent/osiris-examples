package ws.osiris.example.java.localserver

import ws.osiris.example.java.core.api
import ws.osiris.example.java.core.createComponents
import ws.osiris.localserver.runLocalServer

fun main() {
    val components = createComponents()
    runLocalServer(api, components, staticFilesDir = "core/src/main/static")
}
