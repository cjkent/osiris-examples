package io.github.cjkent.osiris.example.customauth1.localserver

import io.github.cjkent.osiris.localserver.runLocalServer

import io.github.cjkent.osiris.example.customauth1.core.api
import io.github.cjkent.osiris.example.customauth1.core.createComponents

fun main(args: Array<String>) {
    val api = api
    val components = createComponents()
    runLocalServer(api, components, staticFilesDir = "core/src/main/static")
}
