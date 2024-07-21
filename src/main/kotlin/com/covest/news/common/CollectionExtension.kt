package com.covest.news.common

import mu.KotlinLogging

object CollectionExtension {
    private val log = KotlinLogging.logger { }

    fun<T> emptyWithLog(message: String): List<T> {
        log.info { "[EMPTY-LIST] ${message}" }
        return emptyList()
    }
}