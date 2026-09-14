group = "dev.dva11.gboard"

patches {
    about {
        name = "Gboard AMOLED Themes"
        description = "Configurable AMOLED themes for Gboard, including Midnight Red."
        source = "https://github.com/thrkingunknown/gboard-theme-patch"
        author = "Dva.11"
        contact = "https://github.com/thrkingunknown"
        website = "https://github.com/thrkingunknown/gboard-theme-patch"
        license = "GPLv3"
    }
}

// Separate configuration so Gson is available to the patch-list generator
// but is never bundled into the generated Android patch.
val patchListGeneratorClasspath = configurations.create("patchListGeneratorClasspath")

dependencies {
    compileOnly(libs.gson)
    patchListGeneratorClasspath(libs.gson)
}

tasks {
    register<JavaExec>("generatePatchesList") {
        description = "Build patch with patch list"
        dependsOn(build)
        classpath = sourceSets["main"].runtimeClasspath + patchListGeneratorClasspath
        mainClass.set("util.PatchListGeneratorKt")
    }

    publish {
        dependsOn("generatePatchesList")
    }
}
