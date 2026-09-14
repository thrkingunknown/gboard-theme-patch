rootProject.name = "gboard-amoled-themes"

pluginManagement {
    // CI bootstraps the exact Morphe Gradle plugin source locally. Keep the
    // path declaration inside pluginManagement: settings plugin resolution
    // happens before ordinary settings-script variables are initialized.
    val localMorphePlugin = file(".gradle-deps/morphe-patches-gradle-plugin")
    if (localMorphePlugin.isDirectory) {
        includeBuild(localMorphePlugin)
    }
    repositories {
        mavenLocal()
        gradlePluginPortal()
        google()
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/MorpheApp/registry")
            credentials {
                username = providers.gradleProperty("gpr.user").getOrElse(System.getenv("GITHUB_ACTOR") ?: "")
                password = providers.gradleProperty("gpr.key").getOrElse(System.getenv("GITHUB_TOKEN") ?: "")
            }
        }
        maven { url = uri("https://jitpack.io") }
    }
}

plugins {
    id("app.morphe.patches") version "1.3.3"
}

// Use the exact Patcher source locally in CI as well, avoiding a second
// cross-organization GitHub Packages dependency. Local source is optional
// for developer machines; the normal Maven registry remains as a fallback.
val localMorphePatcher = file(".gradle-deps/morphe-patcher")
if (localMorphePatcher.isDirectory) {
    includeBuild(localMorphePatcher) {
        dependencySubstitution {
            substitute(module("app.morphe:morphe-patcher")).using(project(":"))
        }
    }
}
