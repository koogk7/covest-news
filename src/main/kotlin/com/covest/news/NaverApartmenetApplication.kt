package com.covest.news

import com.covest.news.domain.estate.NaverApartmentAdapter
import io.github.cdimascio.dotenv.dotenv
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json


suspend fun main() {
    pushNewsToTelegram()
}

private suspend fun pushNewsToTelegram() {
    val env = dotenv()

    val client = HttpClient(CIO) {
        followRedirects = false
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true // 알 수 없는 키 무시
            })
        }
    }
    val adapter = NaverApartmentAdapter(client)
    val result = adapter.getComplexId("창신쌍용2단지")
    println(result)

    adapter.getArticles(result!!).let {
        println(it)
    }
}
