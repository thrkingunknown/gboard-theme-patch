from pathlib import Path

settings = Path("settings.gradle.kts").read_text()
catalog = Path("gradle/libs.versions.toml").read_text()
assert 'id("app.morphe.patches") version "1.3.3"' in settings
assert 'morphe-patcher = "1.7.0"' in catalog
assert 'url = uri("https://maven.pkg.github.com/MorpheApp/registry")' in settings
assert 'val localMorphePlugin = file(".gradle-deps/morphe-patches-gradle-plugin")' in settings
assert 'includeBuild(localMorphePlugin)' in settings
assert 'val localMorphePatcher = file(".gradle-deps/morphe-patcher")' in settings
assert 'includeBuild(localMorphePatcher)' in settings
assert 'System.getenv("GITHUB_ACTOR")' in settings
assert 'System.getenv("GITHUB_TOKEN")' in settings
print("Morphe toolchain alignment verification passed")
