package com.covest.news.domain.estate.vo

// 평형정보
data class SpaceArea(
    val complexId: String, // 아파트 식별자
    val id: String, // 평타입 식별자
    val name: String, // 평네임
    val supplyArea: Double, // 공급면적
    val exclusiveArea: Double, // 전용면적
)