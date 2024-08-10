package com.covest.news.domain.estate

import com.covest.news.common.CollectionExtension.emptyWithLog
import com.covest.news.domain.estate.naver.ArticlesResponse
import com.covest.news.domain.estate.naver.ComplexResponse
import com.covest.news.domain.estate.naver.RealPriceData
import com.covest.news.domain.estate.naver.RepresentativeArticleInfo
import com.covest.news.domain.estate.vo.SpaceArea
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import mu.KotlinLogging


class NaverApartmentAdapter(
    private val client: HttpClient
) {
    private val log = KotlinLogging.logger { }

    private val jwt =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IlJFQUxFU1RBVEUiLCJpYXQiOjE3MjI4NzM2NTcsImV4cCI6MTcyMjg4NDQ1N30.b86mC2I4xkJS7_zjYhc1aFLZ82qNQwiB4utD9ePXGqE"


    // 매물정보 조회
    suspend fun getAllListing(
        apartmentName: String,
        filter: ApartmentListingFilter? = null,
    ): List<ApartmentListing> {
        val complexId = getComplexId(apartmentName)
            ?: return emptyWithLog("${apartmentName} complexId is null")

        val articles = getArticles(complexId, filter)
            ?: return emptyWithLog("${apartmentName} articles is null")

        return articles.map {
            ApartmentListing(
                id = it.articleNumber,
                name = it.complexName,
                tradeType = it.tradeType,
                dongName = it.dongName,
                supplySpace = it.spaceInfo.supplySpace,
                exclusiveSpace = it.spaceInfo.exclusiveSpace,
                description = it.articleDetail.articleFeatureDescription ?: "정보없음",
                floorInfo = it.articleDetail.floorInfo ?: "정보없음",
                price = when (it.tradeType) {
                    ApartmentListingFilter.TradeType.매매.naver -> it.priceInfo.dealPrice.toLong()
                    ApartmentListingFilter.TradeType.전세.naver -> it.priceInfo.warrantyPrice.toLong()
                    else -> it.priceInfo.dealPrice.toLong()
                        .also { i -> log.warn { "Unknown tradeType ${it.tradeType}" } }
                },
            )
        }.filter { filter?.spaceType?.isContain(it.supplySpace) ?: true }
    }

    suspend fun getComplexId(apartmentName: String): String? {
        val encodedApartmentName = java.net.URLEncoder.encode(apartmentName, "UTF-8")
        val url = "https://m.land.naver.com/search/result/$encodedApartmentName"

        val response: HttpResponse = client.request(url) { method = HttpMethod.Head }
        val locationHeader = response.headers[HttpHeaders.Location]
        return locationHeader?.split("/")?.getOrNull(3)?.split("?")?.getOrNull(0)
    }

    suspend fun getArticles(
        complexId: String,
        filter: ApartmentListingFilter? = null,
    ): List<RepresentativeArticleInfo>? {
        val size = 100
        var url =
            "https://fin.land.naver.com/front-api/v1/complex/article/list?complexNumber=$complexId&size=${size}&userChannelType=MOBILE&page=0"
        if (filter != null) {
            url += "&tradeTypes=${filter.tradeType.naver}"
        }
        log.info { "request url ${url}" }

        val response: HttpResponse = client.get(url) {
            headers {
                append(HttpHeaders.AcceptLanguage, "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
            }
        }

        val articlesResponse: ArticlesResponse = response.body()
        if (articlesResponse.isSuccess.not()) {
            log.error { "Failed to get articles. ${complexId}, response: $articlesResponse" }
            return null
        }

        return articlesResponse.result.list
            .map { it.representativeArticleInfo }
    }

    suspend fun getAllSpaceArea(complexId: String): List<SpaceArea> {
        val url = "https://new.land.naver.com/api/complexes/overview/${complexId}?complexNo=${complexId}"
        log.info { "[getSpaceArea] request url ${url}" }

        val response: HttpResponse = client.get(url) { headers { append(HttpHeaders.Authorization, "Bearer $jwt") } }
        val complexResponse: ComplexResponse = response.body()
        return complexResponse.pyeongs.map {
            SpaceArea(
                complexId = complexId,
                id = it.pyeongNo.toString(),
                name = it.pyeongName2,
                supplyArea = it.supplyArea.toDouble(),
                exclusiveArea = it.exclusiveArea.toDouble(),
            )
        }
    }


    // 5년간 시세 조회
    suspend fun getAllRealPrice(
        complexId: String,
        size: Int = 100,
    ): RealPriceData {
        var url =
            "https://new.land.naver.com/api/complexes/${complexId}/prices/real?complexNo=${complexId}&tradeType=A1&year=5&priceChartChange=false&areaNo=1&addedRowCount=1&type=table"
        log.info { "[getAllRealPrice] request url ${url}" }

        val response: HttpResponse = client.get(url) {
            headers { append(HttpHeaders.Authorization, "Bearer $jwt") }
        }
        return response.body()
    }
}


