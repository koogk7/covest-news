package com.covest.news.routes.estate

import com.covest.news.domain.estate.ApartmentListingFilter
import com.covest.news.domain.estate.ApartmentListingFilter.*
import com.covest.news.domain.estate.ApartmentListingFinder
import com.covest.news.domain.estate.NaverApartmentAdapter
import com.covest.news.domain.estate.toTsv
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json

class ApartmentRouter(
    private val apartmentListingFinder: ApartmentListingFinder,
) {
    // 네이버 아파트 매물 조회
    fun Route.route() {
        get("/apartment-listings/{apartName}") {
            val apartName = call.parameters["apartName"]
                ?: return@get call.respondText("Invalid apartName", status = HttpStatusCode.BadRequest)
            val queryParams = call.request.queryParameters
            val tradeType = queryParams["tradeType"]


            val filter = tradeType?.let {
                ApartmentListingFilter(
                    tradeType = TradeType.valueOf(it),
                    spaceType = SpaceType.of(queryParams["spaceType"])
                )
            }
            val result = apartmentListingFinder.getAll(apartName, filter)
            call.respond(result)
        }

        get("/apartment-listings/{apartName}/tsv") {
            val apartName = call.parameters["apartName"]
                ?: return@get call.respondText("Invalid apartName", status = HttpStatusCode.BadRequest)
            val queryParams = call.request.queryParameters
            val tradeType = queryParams["tradeType"]


            val filter = tradeType?.let {
                ApartmentListingFilter(
                    tradeType = TradeType.valueOf(it),
                    spaceType = SpaceType.of(queryParams["spaceType"])
                )
            }
            val result = apartmentListingFinder
                .getAll(apartName, filter)
                .sortedBy { it.price }
            call.respond(result.toTsv())
        }

    }

    companion object {
        fun create(): ApartmentRouter {
            val client = HttpClient(CIO) {
                followRedirects = false
                install(ContentNegotiation) {
                    json(Json {
                        ignoreUnknownKeys = true // 알 수 없는 키 무시
                    })
                }
            }

            val adapter = NaverApartmentAdapter(client)
            val finder = ApartmentListingFinder(adapter)
            return ApartmentRouter(finder)
        }
    }

}