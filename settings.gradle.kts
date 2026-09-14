rootProject.name = "gboard-amoled-themes"

pluginManagement {
    repositories {
        // CI bootstraps the exact Morphe Gradle plugin into this repository before
        // semantic-release starts. This avoids authenticated GitHub Packages during
        // plugin resolution while keeping the normal Morphe version unchanged.
        maven { url = uri(rootDir.resolve(".gradle-deps/maven-repo")) }
        mavenLocal()
        gradlePluginPortal()
        google()
        maven { url = uri("https://jitpack.io") }
    }
}

plugins {
    id("app.morphe.patches") version "1.3.4"
}

// Use the pinned local Patcher source as a composite build exactly as the official
// Morphe patches template does. This avoids Maven publication and GPG requirements.
val localMorphePatcher = rootDir.resolve(".gradle-deps/morphe-patcher")
if (localMorphePatcher.isDirectory) {
    includeBuild(localMorphePatcher) {
        dependencySubstitution {
            substitute(module("app.morphe:morphe-patcher")).using(project(":"))
        }
    }
}
