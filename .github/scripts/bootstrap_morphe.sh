#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
cd "$ROOT_DIR"

# Git preserves this script as data in some checkouts; never depend on the executable bit.
chmod +x "$ROOT_DIR/.github/scripts/bootstrap_morphe.sh" "$ROOT_DIR/gradlew" 2>/dev/null || true

rm -rf .gradle-deps
mkdir -p .gradle-deps

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

clone_tag MorpheApp/morphe-patches-gradle-plugin v1.3.4 1.3.4 .gradle-deps/morphe-patches-gradle-plugin
clone_tag MorpheApp/morphe-patcher v1.13.0 1.13.0 .gradle-deps/morphe-patcher

test -f .gradle-deps/morphe-patches-gradle-plugin/gradlew
test -f .gradle-deps/morphe-patcher/gradlew
test -f .gradle-deps/morphe-patcher/gradle/wrapper/gradle-wrapper.jar

cp .gradle-deps/morphe-patcher/gradle/wrapper/gradle-wrapper.jar gradle/wrapper/gradle-wrapper.jar
chmod +x gradlew .gradle-deps/morphe-patches-gradle-plugin/gradlew .gradle-deps/morphe-patcher/gradlew

test -f gradle/wrapper/gradle-wrapper.jar
echo "Morphe source composites and wrapper bootstrap are ready."
