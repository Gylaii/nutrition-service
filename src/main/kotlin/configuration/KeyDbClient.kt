package com.gulaii.configuration

import messaging.KeyDBClient
import org.koin.core.annotation.Single

@Single(createdAtStart = true)
fun getKeyDbClient() = KeyDBClient()