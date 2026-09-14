from pathlib import Path

settings = Path("settings.gradle.kts").read_text()
catalog = Path("gradle/libs.versions.toml").read_text()
assert 'id("app.morphe.patches") version "1.3.4"' in settings
assert 'morphe-patcher = "1.13.0"' in catalog
assert 'url = uri("https://maven.pkg.github.com/MorpheApp/registry")' in settings
assert 'System.getenv("GITHUB_ACTOR")' in settings
assert 'System.getenv("GITHUB_TOKEN")' in settings
print("Morphe toolchain alignment verification passed")
