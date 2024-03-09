package com.covest.news



import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

suspend fun main() {
    val client = HttpClient(CIO)
    val response: HttpResponse = client.post("http://globalmonitor.einfomax.co.kr/apis/usa/news/getnewsall")
    println(response.bodyAsText())
}
