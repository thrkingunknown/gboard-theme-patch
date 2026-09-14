rootProject.name = "gboard-amoled-themes"

pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        google()
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/MorpheApp/registry")
            credentials {
                username = providers.gradleProperty("gpr.user").getOrElse(System.getenv("GITHUB_ACTOR"))
                password = providers.gradleProperty("gpr.key").getOrElse(System.getenv("GITHUB_TOKEN"))
            }
        }
        maven { url = uri("https://jitpack.io") }
    }
}

plugins {
    // 1.3.3 is the current Morphe template's known-good settings-plugin version.
    // 1.3.4 was failing during plugin application before project evaluation.
    id("app.morphe.patches") version "1.3.3"
}
