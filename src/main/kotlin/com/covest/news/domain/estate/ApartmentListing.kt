package com.covest.news.domain.estate

import kotlinx.serialization.Serializable

// 아파트 매물
@Serializable
data class ApartmentListing(
    val id: String,
    val tradeType: String,
    val dongName: String,
    val description: String,
    val floorInfo: String,
    val price: Long, // 가격
    val supplySpace: Double, // 공급면적
    val exclusiveSpace: Double, // 전용면적
)