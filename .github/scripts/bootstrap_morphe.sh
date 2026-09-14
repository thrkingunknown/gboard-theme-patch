#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
cd "$ROOT_DIR"

# Git may check scripts out without executable bits. Never depend on that metadata.
chmod +x "$ROOT_DIR/.github/scripts/bootstrap_morphe.sh" "$ROOT_DIR/gradlew" 2>/dev/null || true

rm -rf .gradle-deps
mkdir -p .gradle-deps/maven-repo

clone_tag() {
  local repo="$1"
  local first_tag="$2"
  local second_tag="$3"
  local destination="$4"
  if git ls-remote --exit-code --tags "https://github.com/${repo}.git" "refs/tags/${first_tag}" >/dev/null 2>&1; then
    git clone --depth 1 --branch "${first_tag}" "https://github.com/${repo}.git" "${destination}"
  else
    git clone --depth 1 --branch "${second_tag}" "https://github.com/${repo}.git" "${destination}"
  fi
}

PLUGIN_DIR=.gradle-deps/morphe-patches-gradle-plugin
PATCHER_DIR=.gradle-deps/morphe-patcher
LOCAL_MAVEN_REPO="$ROOT_DIR/.gradle-deps/maven-repo"

clone_tag MorpheApp/morphe-patches-gradle-plugin v1.3.4 1.3.4 "$PLUGIN_DIR"
clone_tag MorpheApp/morphe-patcher v1.13.0 1.13.0 "$PATCHER_DIR"

test -f "$PLUGIN_DIR/gradlew"
test -f "$PATCHER_DIR/gradlew"
test -f "$PATCHER_DIR/gradle/wrapper/gradle-wrapper.jar"

# The published Morphe Gradle plugin enables release signing. We are not
# publishing a release here; we only need an unsigned local copy so the root settings
# plugin can resolve without GitHub Packages credentials or a signing key.
python3 - "$PLUGIN_DIR/build.gradle.kts" <<'PY'
from pathlib import Path
import re
import sys

path = Path(sys.argv[1])
text = path.read_text(encoding="utf-8")

# Remove the signing plugin from the plugins block.
text, count = re.subn(r"(?m)^\s*signing\s*\n", "", text, count=1)
assert count == 1, "Expected the Morphe plugin build to apply the signing plugin"

# Remove the top-level signing { ... } configuration block using balanced braces.
match = re.search(r"(?m)^signing\s*\{", text)
assert match, "Expected the Morphe plugin build to contain a signing block"
start = match.start()
brace_start = text.find("{", match.start())
depth = 0
end = None
for i in range(brace_start, len(text)):
    ch = text[i]
    if ch == "{":
        depth += 1
    elif ch == "}":
        depth -= 1
        if depth == 0:
            end = i + 1
            break
assert end is not None, "Could not locate the end of the signing block"
text = text[:start] + text[end:]
path.write_text(text, encoding="utf-8")
PY

chmod +x "$PLUGIN_DIR/gradlew" "$PATCHER_DIR/gradlew"

# Publish only the Gradle plugin to an isolated repository owned by this workflow.
# The transient plugin checkout has no signing task/plugin in the temporary source checkout.
(
  cd "$PLUGIN_DIR"
  ./gradlew publishToMavenLocal --no-daemon --stacktrace -Dmaven.repo.local="$LOCAL_MAVEN_REPO"
)

PLUGIN_MARKER="$LOCAL_MAVEN_REPO/app/morphe/patches/app.morphe.patches.gradle.plugin/1.3.4/app.morphe.patches.gradle.plugin-1.3.4.pom"
PLUGIN_IMPL="$LOCAL_MAVEN_REPO/app/morphe/morphe-patches-gradle-plugin/1.3.4/morphe-patches-gradle-plugin-1.3.4.jar"
test -f "$PLUGIN_MARKER"
test -f "$PLUGIN_IMPL"

# Keep the root Gradle wrapper deterministic by using the pinned Patcher wrapper jar.
cp "$PATCHER_DIR/gradle/wrapper/gradle-wrapper.jar" gradle/wrapper/gradle-wrapper.jar
chmod +x gradlew

test -f gradle/wrapper/gradle-wrapper.jar
echo "Morphe bootstrap ready: local plugin repository + Patcher composite."
