package com.covest.news.domain.estate.naver

import kotlinx.serialization.Serializable

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