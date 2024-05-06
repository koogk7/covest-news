package com.covest.news.domain.balance.models

import com.covest.news.domain.balance.values.Money
import com.covest.news.serialize.BigDecimalSerializer
import com.covest.news.serialize.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.`java-time`.datetime
import java.math.BigDecimal
import java.time.LocalDateTime

@Serializable
data class Balance(
    val id: Int,
    val account: String,
    val productId: String,
    val productName: String,
    @Serializable(with = BigDecimalSerializer::class)
    val qty: BigDecimal,
    val buyAmount: Money,
) {
    @Serializable(with = LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime = LocalDateTime.now()

    @Serializable(with = LocalDateTimeSerializer::class)
    var updatedAt: LocalDateTime = LocalDateTime.now()
}

object BalanceTable : Table("balances") {
    val id: Column<Int> = integer("id").autoIncrement()
    val account: Column<String> = varchar("account", 255)
    val productId: Column<String> = varchar("product_id", 255)
    val productName: Column<String> = varchar("product_name", 255)
    val qty: Column<BigDecimal> = decimal("qty", precision = 10, scale = 2)

    // Money 타입의 buyAmount를 별도의 컬럼으로 나누어 저장
    val buyAmountKrw: Column<BigDecimal> = decimal("buy_amount_krw", precision = 18, scale = 2)
    val buyAmountForeign: Column<BigDecimal> = decimal("buy_amount_foreign", precision = 18, scale = 2)
    val buyAmountCurrency: Column<String> = varchar("buy_amount_currency", 10)

    // Timestamps
    val createdAt: Column<LocalDateTime> = datetime("created_at").clientDefault { LocalDateTime.now() }
    val updatedAt: Column<LocalDateTime> = datetime("updated_at").clientDefault { LocalDateTime.now() }

    override val primaryKey = PrimaryKey(id)
}