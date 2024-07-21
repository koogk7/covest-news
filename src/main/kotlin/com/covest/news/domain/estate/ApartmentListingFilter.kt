package com.covest.news.domain.estate

data class ApartmentListingFilter(
    val tradeType: TradeType,
    val spaceType: SpaceType,
) {
    enum class TradeType(val naver: String) {
        매매("A01"),
        전세("B1"),
    }

    enum class SpaceType {
        _20평_25평,
        _30평_35평;

        fun toSquareMeter(): Pair<Double, Double> {
            return when (this) {
                _20평_25평 -> Pair(66.12, 82.64)
                _30평_35평 -> Pair(99.17, 115.69)
            }
        }

        fun isContain(squareMeter: Double): Boolean {
            val (min, max) = toSquareMeter()
            return squareMeter in min..max
        }
    }
}
