package ws.osiris.example.binary.localserver

import com.beust.jcommander.JCommander
import ws.osiris.example.binary.core.api
import ws.osiris.example.binary.core.config
import ws.osiris.example.binary.core.createComponents
import ws.osiris.localserver.ServerArgs
import ws.osiris.localserver.runLocalServer

fun main(args: Array<String>) {
    val serverArgs = ServerArgs()
    JCommander.newBuilder().addObject(serverArgs).build().parse(*args)
    val api = api
    val components = createComponents()
    runLocalServer(api, components, config, serverArgs.port, serverArgs.root, "core/src/main/static")
}
