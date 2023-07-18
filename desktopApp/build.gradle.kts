import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

kotlin {
    jvm()
    sourceSets {
        val jvmMain by getting  {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(project(":shared"))
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "cz.marvincz.canlii.desktop.ApplicationKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "CanLII Connects no-AI"
            packageVersion = "1.0.0"

            windows {
                upgradeUuid = "b60b3977-0763-4adc-a0bb-15021b69dd3f"
                perUserInstall = true
                menu = true
            }
        }
    }
}