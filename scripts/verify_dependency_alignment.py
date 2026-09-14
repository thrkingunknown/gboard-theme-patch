from pathlib import Path

catalog = Path("gradle/libs.versions.toml").read_text()
settings = Path("settings.gradle.kts").read_text()
build = Path("patches/build.gradle.kts").read_text()
generator = Path("patches/src/main/kotlin/util/PatchListGenerator.kt").read_text()

assert 'morphe-patcher = "1.7.0"' in catalog
assert 'id("app.morphe.patches") version "1.3.3"' in settings
assert 'mavenLocal()' in settings
assert 'val localMorphePlugin = file(".gradle-deps/morphe-patches-gradle-plugin")' in settings
assert 'val localMorphePatcher = file(".gradle-deps/morphe-patcher")' in settings
assert '-Xcontext-parameters' not in build
assert 'patch.category' not in generator
assert 'target.minSdk' not in generator
assert 'target.description' not in generator
assert 'targets = emptyList()' in generator
for path in [catalog, settings, build, generator]:
    assert '1.13.0' not in path
print("Dependency alignment verification passed")
