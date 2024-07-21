package com.covest.news.domain.estate

import kotlinx.serialization.Serializable

// 아파트 매물
@Serializable
data class ApartmentListing(
    val id: String,
    val name: String,
    val tradeType: String,
    val dongName: String,
    val description: String,
    val floorInfo: String,
    val price: Long, // 가격
    val supplySpace: Double, // 공급면적
    val exclusiveSpace: Double, // 전용면적
) {
    fun generateLink(): String {
        return "https://fin.land.naver.com/articles/${id}"
    }
}

fun List<ApartmentListing>.toTsv(): String {
    val header = listOf("이름", "매물가격", "동/층", "공급면적/전용면적", "비고", "링크").joinToString("\t")
    val rows = this.joinToString("\n") { it ->
        listOf(it.name, it.price, "${it.dongName} ${it.floorInfo}", "${it.supplySpace}/${it.exclusiveSpace}", it.description, it.generateLink())
            .joinToString("\t")
    }
    return "$header\n$rows"
}
