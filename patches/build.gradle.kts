group = "dev.thrkingunknown.gboard"

patches {
    about {
        name = "Gboard AMOLED Themes"
        description = "Configurable AMOLED themes for Gboard, with Midnight Red defaults."
        source = "https://github.com/thrkingunknown/gboard-theme-patch"
        author = "thrkingunknown"
        contact = "https://github.com/thrkingunknown"
        website = "https://github.com/thrkingunknown/gboard-theme-patch"
        license = "GPLv3"
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
