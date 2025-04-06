package com.gulaii.listener

import com.gulaii.configuration.KeyDbClient
import io.lettuce.core.pubsub.RedisPubSubAdapter
import org.koin.core.annotation.Single
import org.slf4j.LoggerFactory

@Single(createdAtStart = true)
class NutritionServiceRequestsListener(keyDb: KeyDbClient) {
    private val logger = LoggerFactory.getLogger(this::class.java)
    init {
        keyDb.pubSubConnection.async().subscribe("nutrition-service:request-queue")
        keyDb.pubSubConnection.addListener(object : RedisPubSubAdapter<String, String>() {
            override fun message(channel: String?, message: String?) {
                if (channel == "nutrition-service:request-queue" && message != null) {
                    logger.info("Получено сообщение в Pub/Sub канале: $message")
                }
            }
        })
    }
}