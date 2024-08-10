package com.covest.news.domain.estate.naver

import kotlinx.serialization.Serializable

// 단지 정보
@Serializable
data class ComplexResponse(
    val complexTypeName: String,
    val complexType: String,
    val complexName: String,
    val complexNo: String,
    val totalHouseHoldCount: Int,
    val totalDongCount: Int,
    val useApproveYmd: String,
    val minArea: Double,
    val maxArea: Double,
    val minPrice: Int,
    val maxPrice: Int,
    val minLeasePrice: Int,
    val maxLeasePrice: Int,
    val minPriceByLetter: String,
    val maxPriceByLetter: String,
    val minLeasePriceByLetter: String? = null,
    val maxLeasePriceByLetter: String? = null,
    val leasePerDealRate: Double,
    val isaleDealRestrictionCode: String,
    val rebuildMembershipTransYn: String,
    val livingResidenceYn: String,
    val latitude: Double,
    val longitude: Double,
    val realPrice: RealPrice,
    val pyeongs: List<Pyeong>,
    val dongs: List<Dong>,
    val complexExistTabs: List<String>
) {
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
        val formattedTradeYearMonth: String
    )

    @Serializable
    data class Pyeong(
        val pyeongNo: Int,
        val supplyAreaDouble: Double,
        val supplyArea: String,
        val pyeongName: String,
        val pyeongName2: String,
        val grandPlanUrl: String,
        val exclusiveArea: String,
        val exclusivePyeong: String
    )

    @Serializable
    data class Dong(
        val dongNo: String,
        val bildName: String,
        val highFloor: Int,
        val lowFloor: Int,
        val sortNo: String
    )
}

