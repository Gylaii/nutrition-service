plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlin.plugin.serialization)
    id("com.google.devtools.ksp") version "2.0.21-1.0.28"
}

group = "com.gulaii"
version = "0.0.1"

application {
    mainClass = "io.ktor.server.netty.EngineMain"

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    google()
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

val koinVersion = "4.0.2"
val koinAnnotationsVersion = "2.0.0"

dependencies {
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.jackson)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.postgresql)
    implementation(libs.ktor.server.netty)
    implementation(libs.logback.classic)
    implementation(libs.ktor.server.config.yaml)
    implementation(libs.hikaricp)
    implementation("io.ktor:ktor-server-netty")
    implementation("io.insert-koin:koin-ktor:$koinVersion")
    implementation("io.insert-koin:koin-annotations:$koinAnnotationsVersion")
    implementation("io.lettuce:lettuce-core:6.2.4.RELEASE")
    implementation(libs.clickhouse)
    implementation("io.ktor:ktor-client-core:1.5.2")
    implementation("io.ktor:ktor-client-cio:1.5.2")
    implementation("io.ktor:ktor-client-logging:${libs.versions.ktor.version}")
    implementation("io.ktor:ktor-client-content-negotiation:3.1.2")
    implementation("com.github.Gylaii:keydb-client-lib:v0.1.1")
    ksp("io.insert-koin:koin-ksp-compiler:$koinAnnotationsVersion")
    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.kotlin.test.junit)
}
