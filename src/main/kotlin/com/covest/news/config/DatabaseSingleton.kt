package com.covest.news.config

import com.covest.news.domain.examples.models.Articles
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.*

object DatabaseSingleton {
    fun init(
        databaseName: String,
        user: String,
        password: String,
    ) {
        val driverClassName = "com.mysql.cj.jdbc.Driver"
        val jdbcURL = "jdbc:mysql://localhost:3306/${databaseName}?serverTimezone=UTC"
        val database = Database.connect(jdbcURL, driverClassName, user, password)

        transaction(database) {
            SchemaUtils.create(Articles)
        }
    }
}
