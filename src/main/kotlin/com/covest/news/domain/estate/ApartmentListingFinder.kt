package com.covest.news.domain.estate

class ApartmentListingFinder(
    private val naverApartmentAdapter: NaverApartmentAdapter
) {

    suspend fun getAll(
        apartName: String,
        filter: ApartmentListingFilter? = null,
    ): List<ApartmentListing> {
        return naverApartmentAdapter.getAllListing(apartName, filter)
    }
}