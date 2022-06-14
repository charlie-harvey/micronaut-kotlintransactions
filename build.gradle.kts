 plugins {
    id("org.jetbrains.kotlin.jvm") version "1.6.21"
    id("org.jetbrains.kotlin.kapt") version "1.6.21"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.6.21"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("io.micronaut.application") version "3.4.1"
}

version = "0.1"
group = "com.example"

val kotlinVersion=project.properties.get("kotlinVersion")
repositories {
    mavenCentral()
    maven { url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/") }
}

dependencies {

    kapt("io.micronaut.data:micronaut-data-processor:3.5.0-SNAPSHOT")
    kapt("io.micronaut:micronaut-http-validation")

    implementation("io.micronaut:micronaut-http-client")
    implementation("io.micronaut:micronaut-jackson-databind")
    implementation("io.micronaut.data:micronaut-data-jdbc:3.5.0-SNAPSHOT")
    implementation("io.micronaut.data:micronaut-data-r2dbc:3.5.0-SNAPSHOT")
    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")
    implementation("io.micronaut.sql:micronaut-jdbc-hikari")
    implementation("io.micronaut.liquibase:micronaut-liquibase")
    implementation("io.micronaut:micronaut-validation")

    implementation("jakarta.annotation:jakarta.annotation-api")

    implementation("org.jetbrains.kotlin:kotlin-reflect:${kotlinVersion}")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${kotlinVersion}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk9:1.6.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactive:1.6.1")

    runtimeOnly("com.fasterxml.jackson.module:jackson-module-kotlin")

    runtimeOnly("ch.qos.logback:logback-classic")
    runtimeOnly("dev.miku:r2dbc-mysql")
    runtimeOnly("io.r2dbc:r2dbc-pool:0.9.0.RELEASE")
    runtimeOnly("mysql:mysql-connector-java")

    implementation("com.github.f4b6a3:uuid-creator:4.6.1")
    implementation("io.github.microutils:kotlin-logging-jvm:2.1.23")
    implementation("at.favre.lib:bcrypt:0.9.0")

    kaptTest("io.micronaut:micronaut-inject-java")
    testImplementation(enforcedPlatform("io.micronaut.test:micronaut-test-bom:3.1.1"))
    testImplementation("io.micronaut.test:micronaut-test-kotest")
    testImplementation("io.kotest:kotest-assertions-core-jvm:4.6.4")
    testImplementation("io.kotest:kotest-runner-junit5-jvm:4.6.4")
    testImplementation("io.kotest:kotest-property:4.6.4")
    testImplementation("io.mockk:mockk:1.12.4")
    testImplementation("org.testcontainers:mysql")
    testImplementation("org.testcontainers:r2dbc")
    testImplementation("org.testcontainers:testcontainers")

}


application {
    mainClass.set("com.example.ApplicationKt")
}
java {
    sourceCompatibility = JavaVersion.toVersion("11")
}

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = "11"
        }
    }
    compileTestKotlin {
        kotlinOptions {
            jvmTarget = "11"
        }
    }
}
graalvmNative.toolchainDetection.set(false)
micronaut {
    runtime("netty")
    testRuntime("kotest")
    processing {
        incremental(true)
        annotations("com.example.*")
    }
}



