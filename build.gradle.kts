import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.ktor)
    id("app.cash.sqldelight") version "2.0.2"
}

sqldelight {
    databases {
        create("LocalTrackDatabase") {
            packageName.set("me.znotchill.localtrack.db")
        }
    }
}


group = "me.znotchill"
version = "1.0.0"

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://repo.znotchill.me/repository/maven-releases/")
}

kotlin {
//    jvm()
//    macosArm64()
//    linuxArm64()
    linuxX64()
//    mingwX64()

    targets.withType<KotlinNativeTarget>().configureEach {
        binaries {
            executable {
                entryPoint = "me.znotchill.localtrack.main"
                linkerOpts("-L/usr/lib", "-Wl,--allow-shlib-undefined")
            }
        }
    }

    sourceSets {
        nativeMain.dependencies {
            implementation(libs.logback)
            implementation(libs.ktor.server.core)
            implementation(libs.ktor.server.content.negotiation)
            implementation(libs.ktor.server.cio)
            implementation(libs.ktor.client.cio)
            implementation(libs.ktor.client.websockets)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.network)
            implementation(libs.ktor.network.tls)
            implementation(libs.ktor.client.core)
            implementation("me.znotchill.kiwi:core:1.0.0")
            implementation("me.znotchill.kiwi:network:1.0.0")
            implementation("app.cash.sqldelight:native-driver:2.0.2")
        }
    }
}