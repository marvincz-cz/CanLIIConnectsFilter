pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "CanLII_Connects_Filter"
include(":androidApp")
include(":desktopApp")
include(":shared")
