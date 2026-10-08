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
//    val hostOs = System.getProperty("os.name")
//    val arch = System.getProperty("os.arch")
//    when {
//        hostOs == "Mac OS X" && arch == "x86_64" -> macosX64("native")
//        hostOs == "Mac OS X" && arch == "aarch64" -> macosArm64("native")
//        hostOs == "Linux" && (arch == "x86_64" || arch == "amd64") -> linuxX64("native")
//        hostOs == "Linux" && arch == "aarch64" -> linuxArm64("native")
//        hostOs.startsWith("Windows") -> mingwX64("native")
//        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
//    }

    linuxX64()

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
            implementation("co.touchlab:kermit:2.2.0")
            implementation(libs.ktor.server.core)
            implementation(libs.ktor.server.content.negotiation)
            implementation(libs.ktor.server.cors)
            implementation(libs.ktor.server.cio)
            implementation(libs.ktor.client.cio)
            implementation(libs.ktor.client.websockets)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.network)
            implementation(libs.ktor.network.tls)
            implementation(libs.ktor.client.core)
            implementation("me.znotchill.kiwi:core:1.0.0")
            implementation("me.znotchill.kiwi:network:1.0.0")
            implementation("me.znotchill:kelp:1.0.0")
            implementation("io.github.smyrgeorge:sqlx4k:1.13.0")
            implementation("io.github.smyrgeorge:sqlx4k-sqlite:1.13.0")
            implementation("com.squareup.okio:okio:3.18.1")
            implementation("com.varabyte.kotter:kotter:1.4.0")
            implementation("com.akuleshov7:ktoml-core:0.7.1")
            implementation("com.akuleshov7:ktoml-file:0.7.1")
        }
    }
}