package com.covest.news.plugins

import com.covest.news.routes.example.customerRouting
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureCustomerRouting() {
    routing {
        customerRouting()
    }
}