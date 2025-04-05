package com.gulaii.migrations

import com.gulaii.repository.TestRepository
import org.koin.core.annotation.Single

@Single(createdAtStart = true)
class PostgresMigrations(testRepository: TestRepository) {
    init {
        testRepository.initialize()
    }
}