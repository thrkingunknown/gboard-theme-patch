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

// Use the pinned local Patcher source exactly as the official Morphe template does.
// This avoids Maven/GPG publication entirely during CI bootstrap.
val localMorphePatcher = rootDir.resolve(".gradle-deps/morphe-patcher")
if (localMorphePatcher.isDirectory) {
    includeBuild(localMorphePatcher) {
        dependencySubstitution {
            substitute(module("app.morphe:morphe-patcher")).using(project(":"))
        }
    }
}
