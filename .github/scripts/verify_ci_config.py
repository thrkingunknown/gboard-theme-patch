from pathlib import Path
import json

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
    ("git push origin HEAD:refs/heads/dev", release),
    ("Sync release metadata to release branch", release),
    ("git push origin \"refs/tags/${TAG}:refs/heads/${BRANCH}\"", release),
]
for needle, haystack in checks:
    assert needle in haystack, needle
assert "npm ci" not in release
assert "actions/cache@" not in release
assert Path("patches/src/main/kotlin/util/PatchListGenerator.kt").exists()
for metadata in ("patches-bundle.json", "patches-list.json"):
    assert Path(metadata).exists(), metadata
json.loads(Path("patches-bundle.json").read_text())
json.loads(Path("patches-list.json").read_text())
bundle = json.loads(Path("patches-bundle.json").read_text())
assert bundle["version"] == "1.1.0"
assert bundle["download_url"].endswith("/v1.1.0/patches-1.1.0.mpp")
print("CI/release/cache/generator configuration verification passed")
