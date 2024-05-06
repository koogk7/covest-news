package com.covest.news

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    configureRouting()
    configureSerialization()
}

fun Application.configureRouting() {
    routing {
        customerRouting()
    }
}

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json()
    }
}

fun Route.customerRouting() {
    route("/customer") {
        get {
            val customer = Customer("id", "first", "last", "email")
            call.respond(customer)
        }
    }
}



@Serializable
data class Customer(val id: String, val firstName: String, val lastName: String, val email: String)