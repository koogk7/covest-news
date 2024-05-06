package com.covest.news

import com.covest.news.plugins.configureCustomerRouting
import com.covest.news.plugins.configureSerialization
import io.ktor.server.application.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    configureCustomerRouting()
    configureSerialization()
}