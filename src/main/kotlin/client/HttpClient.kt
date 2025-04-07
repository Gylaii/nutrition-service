package com.gulaii.client

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.jackson.jackson
import org.koin.core.annotation.Single

@Single(createdAtStart = true)
fun getHttpClient(): HttpClient {
    val httpClient = HttpClient {
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
        install(HttpTimeout) {
            connectTimeoutMillis = 10000
        }
        install(ContentNegotiation) {
            jackson()
        }
    }
    return httpClient
}