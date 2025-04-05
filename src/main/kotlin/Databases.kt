package com.gulaii

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.Application
import javax.sql.DataSource

fun getPostgresDataSource(application: Application): DataSource {
    val config = HikariConfig().apply {
        jdbcUrl = application.environment.config.property("postgres.url").getString()
        driverClassName = "org.postgresql.Driver"
        username = application.environment.config.property("postgres.user").getString()
        password = application.environment.config.property("postgres.password").getString()
        maximumPoolSize = 10
        connectionTestQuery = "SELECT 1"
    }

    return HikariDataSource(config)
}

fun getClickHouseDataSource(application: Application): DataSource {
    val config = HikariConfig().apply {
        jdbcUrl = application.environment.config.property("clickhouse.url").getString()
        driverClassName = "ru.yandex.clickhouse.ClickHouseDriver"
        username = application.environment.config.property("clickhouse.user").getString()
        password = application.environment.config.property("clickhouse.password").getString()
        maximumPoolSize = 10
        connectionTestQuery = "SELECT 1"
    }

    return HikariDataSource(config)
}

