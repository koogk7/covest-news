package com.covest.news.routes.example

import com.covest.news.domain.balance.models.Balance
import com.covest.news.domain.balance.models.BalanceTable
import com.covest.news.domain.balance.values.Money
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

fun Route.balanceRouting() {
    post("/balances") {
            val balance = call.receive<Balance>()
            val insertedId = transaction {
                BalanceTable.insert {
                    it[account] = balance.account
                    it[productId] = balance.productId
                    it[productName] = balance.productName
                    it[qty] = balance.qty
                    it[buyAmountKrw] = balance.buyAmount.krw
                    it[buyAmountForeign] = balance.buyAmount.foreign
                    it[buyAmountCurrency] = balance.buyAmount.currency
                    it[createdAt] = balance.createdAt
                    it[updatedAt] = balance.updatedAt
                } get BalanceTable.id
            }
            call.respondText("Balance created with ID: $insertedId")
        }

    get("/balances/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)
                return@get
            }
            val balance = transaction {
                BalanceTable.select { BalanceTable.id eq id }
                    .map {
                        Balance(
                            id = it[BalanceTable.id],
                            account = it[BalanceTable.account],
                            productId = it[BalanceTable.productId],
                            productName = it[BalanceTable.productName],
                            qty = it[BalanceTable.qty],
                            buyAmount = Money(
                                krw = it[BalanceTable.buyAmountKrw],
                                foreign = it[BalanceTable.buyAmountForeign],
                                currency = it[BalanceTable.buyAmountCurrency],
                            )
                        )
                    }.singleOrNull()
            }
            if (balance != null) {
                call.respond(balance)
            } else {
                call.respondText("No balance found with ID: $id", status = HttpStatusCode.NotFound)
            }
        }

}
