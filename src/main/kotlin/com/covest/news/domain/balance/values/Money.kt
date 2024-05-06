package com.covest.news.domain.balance.values

import com.covest.news.serialize.BigDecimalSerializer
import kotlinx.serialization.Serializable
import java.math.BigDecimal
import java.util.*

@Serializable
data class Money(
    @Serializable(with = BigDecimalSerializer::class)
    val krw: BigDecimal,
    @Serializable(with = BigDecimalSerializer::class)
    val foreign: BigDecimal,
    val currency: String,
)