rootProject.name = "gboard-amoled-themes"

pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        google()
        // The Morphe plugin is bootstrapped into mavenLocal by CI before any
        // Gradle invocation, so CI does not depend on authenticated GitHub Packages.
        maven { url = uri("https://jitpack.io") }
    }
}

plugins {
    id("app.morphe.patches") version "1.3.3"
}
