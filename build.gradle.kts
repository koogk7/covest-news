plugins {
    kotlin("jvm") version "1.8.0"
    kotlin("plugin.serialization") version "1.9.22"
    id("io.ktor.plugin") version "2.3.10"
}

val kotlin_version = "1.5.31"
val ktor_version: String = "2.3.10"
val logback_version = "1.2.6"
val exposed_version = "0.41.1"
val h2_version = "2.2.224"

dependencies {
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")

    implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
    implementation("io.github.cdimascio:dotenv-kotlin:6.2.2")

    // ktor servcer
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-test-host:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")

    // exposed
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-java-time:0.34.1")
    implementation("com.h2database:h2:$h2_version")
    implementation("mysql:mysql-connector-java:8.0.19")

    // log
    implementation("io.github.microutils:kotlin-logging-jvm:2.0.10")


    implementation (kotlin("stdlib-jdk8"))
}

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(11)
}