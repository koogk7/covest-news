package com.covest.news.routes.example

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
data class Customer(val id: String, val firstName: String, val lastName: String, val email: String)

fun Route.customerRouting() {
    route("/customer") {
        get {
            val customer = Customer("id", "first", "last", "email")
            call.respond(customer)
        }
    }
}
