rootProject.name = "gboard-amoled-themes"

pluginManagement {
    val localMorphePlugin = rootDir.resolve(".gradle-deps/morphe-patches-gradle-plugin")
    if (localMorphePlugin.isDirectory) {
        includeBuild(localMorphePlugin)
    }

    repositories {
        mavenLocal()
        gradlePluginPortal()
        google()
        maven { url = uri("https://jitpack.io") }
    }
}

plugins {
    id("app.morphe.patches") version "1.3.4"
}
