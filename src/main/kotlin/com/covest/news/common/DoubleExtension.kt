package com.covest.news.common

object DoubleExtension {

    fun Double.between(min: Double, max: Double): Boolean {
        return this in min..max
    }

}