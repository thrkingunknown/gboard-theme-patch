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
assert "github.ref_name == 'main' || github.ref_name == 'dev'" in release
assert "actions/cache@" not in release
assert Path("patches/src/main/kotlin/util/PatchListGenerator.kt").exists()
for metadata in ("patches-bundle.json", "patches-list.json"):
    assert Path(metadata).exists(), metadata
json.loads(Path("patches-bundle.json").read_text())
json.loads(Path("patches-list.json").read_text())
bundle = json.loads(Path("patches-bundle.json").read_text())
version_match = __import__("re").search(r"(?m)^version=(.+)$", props)
assert version_match, "gradle.properties must define version"
project_version = version_match.group(1).strip()
assert bundle["version"] == project_version, (
    f"patches-bundle.json version {bundle['version']} != project version {project_version}"
)
assert bundle["download_url"].endswith(
    f"/v{project_version}/patches-{project_version}.mpp"
), "patches-bundle.json download_url does not match project version"
assert json.loads(Path("patches-list.json").read_text())["version"] == project_version
assert bundle.get("created_at"), "patches-bundle.json missing created_at"
assert isinstance(bundle.get("description"), str) and bundle["description"].strip(), "patches-bundle.json missing description"
assert bundle.get("download_url", "").startswith("https://github.com/")
assert bundle.get("signature_download_url", "") == "" or bundle["signature_download_url"].startswith("https://github.com/")
print("CI/release/cache/generator configuration verification passed")
