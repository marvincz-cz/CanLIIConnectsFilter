plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("com.android.library")
    id("org.jetbrains.compose")
    id("dev.icerock.mobile.multiplatform-resources") version "0.23.0"
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    targetHierarchy.default()

    android {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }

    jvm("desktop")
    
//    listOf(
//        iosX64(),
//        iosArm64(),
//        iosSimulatorArm64()
//    ).forEach {
//        it.binaries.framework {
//            baseName = "shared"
//        }
//    }

    sourceSets {
        val ktorVersion = "2.3.2"
        val koinVersion = "3.4.0"
        val coroutinesVersion = "1.7.1"

        val commonMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")

                implementation("com.mohamedrejeb.ksoup:ksoup-html:0.1.3")

                implementation("com.russhwolf:multiplatform-settings:1.0.0")
                api("io.insert-koin:koin-core:$koinVersion")
                implementation("io.insert-koin:koin-compose:1.0.3")
                api("dev.icerock.moko:resources:0.23.0")
                api("dev.icerock.moko:resources-compose:0.23.0")

                api("com.arkivanov.decompose:decompose:2.0.0-compose-experimental")
                api("com.arkivanov.decompose:extensions-compose-jetbrains:2.0.0-compose-experimental")

                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
                api("io.insert-koin:koin-android:$koinVersion")

                api("androidx.activity:activity-compose:1.7.2")
                api("androidx.appcompat:appcompat:1.6.1")
                api("androidx.core:core-ktx:1.10.1")
            }
        }
//        val iosX64Main by getting
//        val iosArm64Main by getting
//        val iosSimulatorArm64Main by getting
//        val iosMain by getting {
//            iosX64Main.dependsOn(this)
//            iosArm64Main.dependsOn(this)
//            iosSimulatorArm64Main.dependsOn(this)
//            dependsOn(commonMain)
//            dependencies {
//                implementation("io.ktor:ktor-client-darwin:$ktorVersion")
//            }
//        }
        val desktopMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
                implementation(compose.desktop.common)
                api("org.jetbrains.kotlinx:kotlinx-coroutines-swing:$coroutinesVersion")
            }
        }
    }
}

android {
    namespace = "cz.marvincz.canlii"
    compileSdk = 33
    defaultConfig {
        minSdk = 26
    }
}

multiplatformResources {
    multiplatformResourcesPackage = "cz.marvincz.canlii"
}