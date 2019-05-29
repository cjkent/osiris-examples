package ws.osiris.example.customauth2.localserver

import ws.osiris.example.customauth2.core.api
import ws.osiris.example.customauth2.core.createComponents
import ws.osiris.localserver.runLocalServer

fun main() {
    val components = createComponents()
    runLocalServer(api, components, staticFilesDir = "core/src/main/static")
}
