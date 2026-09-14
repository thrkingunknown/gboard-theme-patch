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
    ("cache-dependency-path: package.json", release),
    ("npm install --no-audit --no-fund", release),
    ("gradle/actions/setup-gradle@v6", release),
    ("cache-read-only: false", release),
    ("org.gradle.caching=true", props),
    ("org.gradle.parallel=true", props),
    ("gradle-semantic-release-plugin", json.dumps(package_json)),
    ("downloadUrlTemplate", releaserc),
    ("prepareCmd", releaserc),
]
for needle, haystack in checks:
    assert needle in haystack, needle
assert "npm ci" not in release
assert "actions/cache@" not in release
print("CI/release/cache configuration verification passed")
