group = "dev.thrkingunknown.gboard"

// The release version is supplied by the root Gradle version during semantic-release.
// Do not override it here, or the generated .mpp filename can remain on an older version.
patches {
    about {
        name = "Gboard AMOLED Theme Studio"
        description = "Standalone, configurable AMOLED themes for Gboard. Midnight Red is the default palette."
        source = "https://github.com/thrkingunknown/gboard-theme-patch"
        author = "thrkingunknown"
        contact = "https://github.com/thrkingunknown"
        website = "https://github.com/thrkingunknown/gboard-theme-patch"
        license = "GPLv3"
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xcontext-parameters")
    }
}

// Match the Morphe template: Gson is available to the generated patch-list task
// but is not bundled into the patch APK.
val patchListGeneratorClasspath = configurations.create("patchListGeneratorClasspath")

dependencies {
    compileOnly(libs.gson)
    patchListGeneratorClasspath(libs.gson)
}

tasks {
    register<JavaExec>("generatePatchesList") {
        description = "Generate the Morphe patch list."
        dependsOn(build)
        classpath = sourceSets["main"].runtimeClasspath + patchListGeneratorClasspath
        mainClass.set("util.PatchListGeneratorKt")
    }

    publish {
        dependsOn("generatePatchesList")
    }
}
