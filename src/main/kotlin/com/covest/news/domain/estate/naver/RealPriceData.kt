package com.covest.news.domain.estate.naver

import kotlinx.serialization.Serializable

@Serializable
data class RealPriceData( // 시세정보
    val areaNo: Int,  // Root JSON field
    val realPriceOnMonthList: List<RealPriceOnMonth>,  // List of RealPriceOnMonth
    val addedRowCount: Int,
    val totalRowCount: Int,
    val realPriceBasisYearMonth: String
)

@Serializable
data class RealPriceOnMonth(
    val realPriceList: List<RealPrice>,  // List of RealPrice within each month
    val tradeBaseYear: String,
    val tradeBaseMonth: Int
)

@Serializable
data class RealPrice(
    val tradeType: String,
    val tradeYear: String,
    val tradeMonth: Int,
    val tradeDate: String,
    val dealPrice: Int,
    val floor: Int,
    val representativeArea: Double,
    val exclusiveArea: Double,
    val formattedPrice: String,
    val formattedTradeYearMonth: String,
    val deleteYn: String? = null  // Nullable field to accommodate absence in some JSON objects
)
