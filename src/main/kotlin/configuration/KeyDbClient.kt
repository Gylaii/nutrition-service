package com.gulaii.configuration

import io.lettuce.core.RedisClient
import io.lettuce.core.api.StatefulRedisConnection
import io.lettuce.core.api.sync.RedisCommands
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection
import org.koin.core.annotation.Single

@Single(createdAtStart = true)
class KeyDbClient {
    private val redisClient = RedisClient.create("redis://localhost:6379")
    private val connection: StatefulRedisConnection<String, String> = redisClient.connect()
    val commands: RedisCommands<String, String> = connection.sync()
    val pubSubConnection: StatefulRedisPubSubConnection<String, String> = redisClient.connectPubSub()
}