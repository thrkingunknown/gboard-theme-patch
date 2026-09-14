from pathlib import Path
import json
import re

release = Path(".github/workflows/release.yml").read_text()
props = Path("gradle.properties").read_text()
releaserc = Path(".releaserc").read_text()
package_json = json.loads(Path("package.json").read_text())

json.loads(releaserc)

checks = [
    ("actions/setup-node@v7", release),
    ("cache: 'npm'", release),
    ("cache-dependency-path: package-lock.json", release),
    ("npm install --no-audit --no-fund", release),
    ("gradle/actions/setup-gradle@v6", release),
    ("cache-read-only: false", release),
    ("org.gradle.caching=true", props),
    ("org.gradle.parallel=true", props),
    ("gradle-semantic-release-plugin", json.dumps(package_json)),
    ("downloadUrlTemplate", releaserc),
    ("prepareCmd", releaserc),
    ("mainClass.set(\"util.PatchListGeneratorKt\")", Path("patches/build.gradle.kts").read_text()),
    ("Ensure Morphe dev branch exists", release),
    ("Verify published Morphe metadata", release),
    ("git push origin HEAD:refs/heads/dev", release),
    ("Bootstrap pinned Morphe toolchain", release),
    ("Bootstrap pinned Morphe toolchain", release),
]
for needle, haystack in checks:
    assert needle in haystack, needle
assert "npm ci" not in release
assert "github.ref_name == 'main' || github.ref_name == 'dev'" in release
assert "actions/cache@" not in release
assert Path("patches/src/main/kotlin/util/PatchListGenerator.kt").exists()
for metadata in ("patches-bundle.json", "patches-list.json"):
    assert Path(metadata).exists(), metadata
json.loads(Path("patches-bundle.json").read_text())
json.loads(Path("patches-list.json").read_text())
bundle = json.loads(Path("patches-bundle.json").read_text())
version_match = re.search(r"(?m)^version=(.+)$", props)
assert version_match, "gradle.properties must define version"
project_version = version_match.group(1).strip()
assert bundle["version"] == project_version
assert bundle["download_url"].endswith(f"/v{project_version}/patches-{project_version}.mpp")
assert json.loads(Path("patches-list.json").read_text())["version"] == project_version

settings_text = Path("settings.gradle.kts").read_text()
assert 'id("app.morphe.patches") version "1.3.4"' in settings_text
assert 'localMorphePlugin' not in settings_text
assert 'maven { url = uri(rootDir.resolve(".gradle-deps/maven-repo")) }' in settings_text
assert 'val localMorphePatcher = rootDir.resolve(".gradle-deps/morphe-patcher")' in settings_text
assert 'substitute(module("app.morphe:morphe-patcher")).using(project(":"))' in settings_text
assert "mavenLocal()" in settings_text
assert "maven.pkg.github.com/MorpheApp/registry" not in settings_text
assert "maven.pkg.github.com/MorpheApp/registry" not in release
assert "substitute(module(\"app.morphe:morphe-patcher\")).using(project(\":\"))" in settings_text
assert "publishToMavenLocal" not in release
assert "publishToMavenLocal" in Path(".github/scripts/bootstrap_morphe.sh").read_text()
assert "maven.repo.local" in Path(".github/scripts/bootstrap_morphe.sh").read_text()
bootstrap = release.index("Bootstrap pinned Morphe toolchain")
release_action = release.index("uses: cycjimmy/semantic-release-action@v6")
assert bootstrap < release_action
print("CI/release/cache/generator configuration passed")
print("Morphe CI bootstrap configuration passed")
