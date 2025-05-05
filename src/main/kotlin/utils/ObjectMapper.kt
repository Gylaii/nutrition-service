package com.gulaii.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.koin.core.annotation.Single

@Single(createdAtStart = true)
fun getObjectMapper(): ObjectMapper = jacksonObjectMapper()