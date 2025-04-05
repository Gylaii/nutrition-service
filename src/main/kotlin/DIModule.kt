package com.gulaii

import com.gulaii.di.AppModule
import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.koin.core.qualifier.named
import org.koin.ksp.generated.module
import org.koin.ktor.plugin.Koin

fun Application.diModule() {
    install(Koin) {
        modules(
            AppModule().module.apply {
                single(createdAtStart = true, qualifier = named("chDataSource")) {
                    getClickHouseDataSource(this@diModule)
                }
                single(createdAtStart = true, qualifier = named("pgDataSource")) {
                    getPostgresDataSource(this@diModule)
                }
            }
        )
    }
}