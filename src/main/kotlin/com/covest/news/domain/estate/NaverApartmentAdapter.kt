package com.covest.news.domain.estate

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.Serializable


class NaverApartmentAdapter(
    private val client: HttpClient
) {
    suspend fun getComplexId(apartmentName: String): String? {
        val encodedApartmentName = java.net.URLEncoder.encode(apartmentName, "UTF-8")
        val url = "https://m.land.naver.com/search/result/$encodedApartmentName"

        val response: HttpResponse = client.request(url) { method = HttpMethod.Head }
        val locationHeader = response.headers[HttpHeaders.Location]
        return locationHeader?.split("/")?.getOrNull(3)?.split("?")?.getOrNull(0)
    }

    suspend fun getArticles(complexId: String): List<RepresentativeArticleInfo>? {
        val url =
            "https://fin.land.naver.com/front-api/v1/complex/article/list?complexNumber=$complexId&pyeongTypeNumbers=4&tradeTypes=A1&size=20&userChannelType=MOBILE&page=0"

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
        return if (articlesResponse.isSuccess) articlesResponse.result.list.map { it.representativeArticleInfo } else null
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
    val articleFeatureDescription: String,
    val directTrade: Boolean,
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

