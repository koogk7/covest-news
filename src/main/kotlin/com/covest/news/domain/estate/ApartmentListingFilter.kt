package com.covest.news.domain.estate

data class ApartmentListingFilter(
    val tradeType: TradeType,
    val spaceType: SpaceType,
) {
    enum class TradeType(val naver: String) {
        매매("A01"),
        전세("B1");

        companion object {

        }

    }

    enum class SpaceType {
        _20평_25평,
        _30평_35평,
        ALL;

        companion object {
            fun of(value: String?): SpaceType {
                return when (value) {
                    "20평_25평" -> _20평_25평
                    "30평_35평" -> _30평_35평
                    else -> ALL
                }
            }
        }

        private fun toSquareMeter(): Pair<Double, Double> {
            return when (this) {
                _20평_25평 -> Pair(66.12, 82.64)
                _30평_35평 -> Pair(99.17, 115.69)
                ALL -> Pair(0.0, Double.MAX_VALUE)
            }
        }

        fun isContain(squareMeter: Double): Boolean {
            val (min, max) = toSquareMeter()
            return squareMeter in min..max
        }
    }
}
