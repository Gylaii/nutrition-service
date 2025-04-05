package com.gulaii.migrations

import com.gulaii.repository.ClickTestRepository
import org.koin.core.annotation.Single

@Single(createdAtStart = true)
class ClickHouseMigrations(clickTestRepository: ClickTestRepository) {
    init {
        clickTestRepository.initialize()
    }
}
