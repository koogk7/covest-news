package com.covest.news.domain.estate

import com.covest.news.common.CollectionExtension.emptyWithLog
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.Serializable
import mu.KotlinLogging


class NaverApartmentAdapter(
    private val client: HttpClient
) {
    private val log = KotlinLogging.logger { }

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
        var url = "https://fin.land.naver.com/front-api/v1/complex/article/list?complexNumber=$complexId&size=${size}&userChannelType=MOBILE&page=0"
        if (filter != null) {
           url += "&tradeTypes=${filter.tradeType.naver}"
        }
        log.info { "request url ${url}" }

        val response: HttpResponse = client.get(url) {
            headers {
                append(HttpHeaders.Accept, "application/json, text/plain, */*")
                append(HttpHeaders.AcceptLanguage, "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
                append(
                    HttpHeaders.UserAgent,
                    "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Mobile Safari/537.36"
                )
            }
        }

        val articlesResponse: ArticlesResponse = response.body()
        if (articlesResponse.isSuccess.not()) {
            log.error { "Failed to get articles. ${complexId}, response: $articlesResponse"  }
            return null
        }

        return articlesResponse.result.list
            .map { it.representativeArticleInfo }
    }
}


@Serializable
data class ArticlesResponse(
    val isSuccess: Boolean,
    val result: Result
)

@Serializable
data class Result(
    val hasNextPage: Boolean,
    val list: List<ArticleItem>,
    val totalCount: Int
)

@Serializable
data class ArticleItem(
    val representativeArticleInfo: RepresentativeArticleInfo,
    val duplicatedArticlesInfo: DuplicatedArticlesInfo? = null
)

@Serializable
data class RepresentativeArticleInfo(
    val complexName: String,
    val articleNumber: String,
    val dongName: String,
    val tradeType: String,
    val realEstateType: String,
    val spaceInfo: SpaceInfo,
    val verificationInfo: VerificationInfo,
    val brokerInfo: BrokerInfo,
    val articleDetail: ArticleDetail,
    val articleMediaDto: ArticleMediaDto? = null,
    val priceInfo: PriceInfo
)

@Serializable
data class SpaceInfo(
    val supplySpace: Double,
    val exclusiveSpace: Double,
    val supplySpaceName: String,
    val exclusiveSpaceName: String,
    val nameType: String
)

@Serializable
data class VerificationInfo(
    val verificationType: String,
    val isAssociationArticle: Boolean,
    val exposureStartDate: String
)

@Serializable
data class BrokerInfo(
    val cpId: String,
    val brokerageName: String,
    val brokerName: String,
    val isCpOutLinked: Boolean
)

@Serializable
data class ArticleDetail(
    val direction: String? = null,
    val articleFeatureDescription: String? = null,
    val directTrade: Boolean? = null,
    val floorInfo: String? = null,
)

@Serializable
data class ArticleMediaDto(
    val imageUrl: String? = null,
    val imageType: String? = null,
    val imageCount: Int = 0
)

@Serializable
data class PriceInfo(
    val dealPrice: Int,
    val warrantyPrice: Int,
    val rentPrice: Int,
    val priceChangeStatus: Int,
    val priceChangeHistories: List<PriceChangeHistory>? = null
)

@Serializable
data class PriceChangeHistory(
    val date: String? = null,
    val price: Int? = null,
)

@Serializable
data class DuplicatedArticlesInfo(
    val representativePriceInfo: RepresentativePriceInfo,
    val realtorCount: Int,
    val directTradeCount: Int,
    val articleInfoList: List<ArticleInfo>
)

@Serializable
data class RepresentativePriceInfo(
    val dealPrice: PriceRange,
    val warrantyPrice: PriceRange,
    val rentPrice: PriceRange,
    val premiumPrice: PriceRange
)

@Serializable
data class PriceRange(
    val minPrice: Int,
    val maxPrice: Int
)

@Serializable
data class ArticleInfo(
    val priceInfo: PriceInfo,
    val verificationInfo: VerificationInfo,
    val brokerInfo: BrokerInfo,
    val articleDetail: ArticleDetail,
    val articleMediaDto: ArticleMediaDto? = null
)

