package io.github.cjkent.osiris.example.java.localserver

import io.github.cjkent.osiris.example.java.core.api
import io.github.cjkent.osiris.example.java.core.createComponents
import io.github.cjkent.osiris.localserver.runLocalServer

fun main(args: Array<String>) {
    val api = api
    val components = createComponents()
    runLocalServer(api, components, staticFilesDir = "core/src/main/static")
}
