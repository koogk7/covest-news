package com.covest.news

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.ExperimentalSerializationApi

suspend fun main() {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
    }
    val response: HttpResponse = client.post("http://globalmonitor.einfomax.co.kr/apis/usa/news/getnewsall")
    println(response.bodyAsText())

    val newsResponse: NewsResponse = client.post("http://globalmonitor.einfomax.co.kr/apis/usa/news/getnewsall").body()
    println(newsResponse)
}

@Serializable
data class NewsResponse(
    val total: Int,
    val data: List<NewsItem>
)

@Serializable
data class NewsItem(
    @SerialName("_id")
    val id: String,
    @SerialName("SendDateTime")
    val sendDateTime: String,
    @SerialName("Credit")
    val credit: String? = null,
    @SerialName("Category.@name")
    val categoryName: String,
    @SerialName("Title")
    val title: String,
    @SerialName("viewcount")
    val viewCount: Int,
    @SerialName("UpdateDateTime")
    val updateDateTime: String,
    @SerialName("Writer")
    val writer: String,
    @SerialName("Source")
    val source: String
)
