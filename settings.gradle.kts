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
                username = providers.gradleProperty("gpr.user")
                    .orElse(providers.environmentVariable("GPR_USER"))
                    .getOrElse(System.getenv("GITHUB_ACTOR"))
                password = providers.gradleProperty("gpr.key")
                    .orElse(providers.environmentVariable("GPR_KEY"))
                    .getOrElse(System.getenv("GITHUB_TOKEN"))
            }
        }
        maven { url = uri("https://jitpack.io") }
    }
}

plugins {
    // 1.3.4 is the current published Morphe settings-plugin release and is required for the Kotlin 2.4 toolchain used by Morphe Patcher 1.13.x.
    id("app.morphe.patches") version "1.3.4"
}

settings {
    extensions {
        defaultNamespace = "app.morphe.extension"
        proguardFiles(rootProject.projectDir.resolve("extensions/proguard-rules.pro").toString())
    }
}
