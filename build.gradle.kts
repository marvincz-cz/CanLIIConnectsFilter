plugins {
    val composeVersion = "1.4.1"

    //trick: for the same plugin versions in all sub-modules
    id("com.android.application").version("8.0.1").apply(false)
    id("com.android.library").version("8.0.1").apply(false)
    kotlin("android").version("1.8.20").apply(false)
    kotlin("multiplatform").version("1.8.20").apply(false)
    kotlin("plugin.serialization").version("1.8.20").apply(false)
    id("org.jetbrains.compose").version(composeVersion).apply(false)
    id("org.jetbrains.kotlin.jvm") version "1.8.20" apply false
}
