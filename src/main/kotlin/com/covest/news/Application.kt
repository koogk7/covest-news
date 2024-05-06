package com.covest.news

import com.covest.news.config.DatabaseSingleton
import com.covest.news.plugins.configureCustomerRouting
import com.covest.news.plugins.configureSerialization
import io.github.cdimascio.dotenv.dotenv
import io.ktor.server.application.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    val env = dotenv()

    DatabaseSingleton.init(
        databaseName = env["DATABASE_NAME"]!!,
        user = env["DATABASE_USER"]!!,
        password = env["DATABASE_PASSWORD"]!!,
    )
    configureCustomerRouting()
    configureSerialization()
}