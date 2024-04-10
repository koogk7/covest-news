package com.covest.news

import com.covest.news.adapter.GlobalMonitorNewsAdapter
import com.covest.news.adapter.TelegramAdapter
import io.github.cdimascio.dotenv.dotenv
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import java.time.LocalDateTime


suspend fun main() {
    val env = dotenv()

    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
    }
    val adapter = GlobalMonitorNewsAdapter(client)
    val telegramAdapter = TelegramAdapter(client, env["TELEGRAM_TOKEN"]!!, env["TELEGRAM_CHAT_ID"]!!)

    val titleAndDate = adapter.getAllNewsHeadline().map { it.title + " - " + it.updatedAt }
    val message = titleAndDate.joinToString(
        prefix = "\n=====================\n",
        separator = "\n=====================\n",
    )
    println(telegramAdapter.send(message))
    println(message)
    /*
        TODO
        1. 중복알림 제거하기 -> 아마 디비를 써야하지 않을까?
        2. 토큰 값 외부 주입받기 (for 보안)
        3. 서버에 올려두고 주기적으로 뉴스보내도록 하기
        4. 해드라인 정보로 공부할만한 키워드 추출하기 with gpt
     */
}

data class NewsHeadline(
    val id: String,
    val title: String,
    val updatedAt: LocalDateTime,
    val writer: String,
    val source: String
)
