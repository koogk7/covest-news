package com.covest.news.adapter

import com.covest.news.NewsHeadline
import com.covest.news.serialize.LocalDateTimeSerializer
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

class GlobalMonitorNewsAdapter(
    private val httpClient: HttpClient
) {
    companion object {
        const val GET_ALL = "http://globalmonitor.einfomax.co.kr/apis/usa/news/getnewsall"
        const val GET_ONE = "https://globalmonitor.einfomax.co.kr/apis/usa/news/getnewsone"
    }

    suspend fun getAllNewsHeadline(): List<com.covest.news.NewsHeadline> {
        return httpClient
            .post(GET_ALL)
            .body<NewsHeadlineResponse>()
            .data
            .map { NewsHeadline(it.id, it.title, it.updateDateTime, it.writer, it.source) }
    }

    suspend fun getOne(id: String): String {
        return httpClient.post(GET_ONE) {
            contentType(ContentType.Application.Json)
            setBody(mapOf("id" to id))
        }.bodyAsText()
    }

    @Serializable
    data class NewsHeadlineResponse(
        val total: Int,
        val data: List<NewsHeadline>
    )

    @Serializable
    data class NewsHeadline(
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
        val viewCount: Int = 0,
        @SerialName("UpdateDateTime")
        @Serializable(with = LocalDateTimeSerializer::class)
        val updateDateTime: LocalDateTime,
        @SerialName("Writer")
        val writer: String,
        @SerialName("Source")
        val source: String
    )
}