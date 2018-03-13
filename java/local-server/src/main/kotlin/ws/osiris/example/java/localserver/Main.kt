package ws.osiris.example.java.localserver

import ws.osiris.example.java.core.api
import ws.osiris.example.java.core.config
import ws.osiris.example.java.core.createComponents
import ws.osiris.localserver.runLocalServer

fun main(args: Array<String>) {
    val components = createComponents()
    runLocalServer(api, components, config, staticFilesDir = "core/src/main/static")
}
