from pathlib import Path

catalog = Path("gradle/libs.versions.toml").read_text()
build = Path("patches/build.gradle.kts").read_text()
settings = Path("settings.gradle.kts").read_text()

assert 'morphe-patcher = "1.13.0"' in catalog
assert 'morphe-patcher = { module = "app.morphe:morphe-patcher", version.ref = "morphe-patcher" }' in catalog
assert 'id("app.morphe.patches") version "1.3.4"' in settings
assert 'name = "Gboard AMOLED Themes"' in build
assert 'contact = "https://github.com/thrkingunknown"' in build
print("Build configuration verification passed")
