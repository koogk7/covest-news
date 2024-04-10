package com.covest.news.adapter

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class TelegramAdapter(
    private val client: HttpClient,
    private val token: String,
    private val chatId: String
) {
    companion object {
        const val BASE_URL = "https://api.telegram.org/bot"
        const val SEND_MESSAGE = "/sendMessage"
    }

    suspend fun send(message: String): String {
       return client.post(BASE_URL + token + SEND_MESSAGE) {
           contentType(ContentType.Application.Json)
           setBody(mapOf("chat_id" to chatId, "text" to message))
        }.bodyAsText() // TODO: 코틀린 철학을 살려서... 리턴까지 응답을 내보내지 않고 status code를 logging 할 수 있을까?
    }

}