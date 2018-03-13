package ws.osiris.example.customauth2.localserver

import ws.osiris.example.customauth2.core.api
import ws.osiris.example.customauth2.core.config
import ws.osiris.example.customauth2.core.createComponents
import ws.osiris.localserver.runLocalServer

fun main(args: Array<String>) {
    val components = createComponents()
    runLocalServer(api, components, config, staticFilesDir = "core/src/main/static")
}
