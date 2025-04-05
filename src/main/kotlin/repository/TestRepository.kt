package com.gulaii.repository

import org.koin.core.annotation.Qualifier
import org.koin.core.annotation.Singleton
import javax.sql.DataSource

interface TestRepository {
    fun initialize()
}

@Singleton(binds = [TestRepository::class], createdAtStart = true)
class TestRepositoryImpl(@Qualifier(name = "pgDataSource") val ds: DataSource) : TestRepository {
    override fun initialize() {
        ds.connection.use {
            it.prepareStatement("""
                CREATE TABLE IF NOT EXISTS CITIES (
                    ID SERIAL PRIMARY KEY, 
                    NAME VARCHAR(255), 
                    POPULATION INT
                );
                """.trimIndent()
            ).execute()
        }
    }
}