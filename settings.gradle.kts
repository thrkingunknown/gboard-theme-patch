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

        // Obtain baksmali/smali from source builds - https://github.com/iBotPeaches/smali
        // Remove when official smali releases come out again.
        maven { url = uri("https://jitpack.io") }
    }
}

plugins {
    // Pin to the version used by the current official Morphe template.
    // 1.3.4 is published, but this project previously failed during plugin application
    // with IllegalArgumentException; keep the known-good template version.
    id("app.morphe.patches") version "1.3.3"
}
