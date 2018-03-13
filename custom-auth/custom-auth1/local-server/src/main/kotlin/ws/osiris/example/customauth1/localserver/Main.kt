package ws.osiris.example.customauth1.localserver

import ws.osiris.example.customauth1.core.api
import ws.osiris.example.customauth1.core.config
import ws.osiris.example.customauth1.core.createComponents
import ws.osiris.localserver.runLocalServer

fun main(args: Array<String>) {
    val components = createComponents()
    runLocalServer(api, components, config, staticFilesDir = "core/src/main/static")
}
