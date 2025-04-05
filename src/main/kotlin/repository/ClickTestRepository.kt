package com.gulaii.repository

import org.koin.core.annotation.Qualifier
import org.koin.core.annotation.Single
import javax.sql.DataSource

interface ClickTestRepository {
    fun initialize()
}

@Single(binds = [ClickTestRepository::class], createdAtStart = true)
class ClickTestRepositoryImpl(@Qualifier(name = "chDataSource") val ds: DataSource) : ClickTestRepository {
    override fun initialize() {
        ds.connection.use {
            it.prepareStatement(
                """
                    CREATE TABLE IF NOT EXISTS authors (
                        id UUID,name String,
                        email String,
                        created_at DateTime
                    ) ENGINE = MergeTree() PRIMARY KEY id;
                """.trimIndent()
            ).execute()
        }
    }
}