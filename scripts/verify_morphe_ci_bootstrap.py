from pathlib import Path

settings = Path("settings.gradle.kts").read_text()
workflow = Path(".github/workflows/release.yml").read_text()
bootstrap = Path(".github/scripts/bootstrap_morphe.sh").read_text()

assert 'id("app.morphe.patches") version "1.3.4"' in settings
assert 'includeBuild(localMorphePlugin)' in settings
assert 'localMorphePatcher' in settings
assert 'substitute(module("app.morphe:morphe-patcher")).using(project(":"))' in settings
assert '.gradle-deps/morphe-patches-gradle-plugin' in bootstrap
assert '.gradle-deps/morphe-patcher' in bootstrap
assert 'publishToMavenLocal' not in bootstrap
assert 'publishToMavenLocal' not in workflow
assert 'maven.pkg.github.com/MorpheApp/registry' not in settings
assert 'maven.pkg.github.com/MorpheApp/registry' not in workflow
assert 'v1.3.4 1.3.4' in bootstrap
assert 'v1.13.0 1.13.0' in bootstrap
assert 'ORG_GRADLE_PROJECT_gpr_user:' in workflow
assert 'ORG_GRADLE_PROJECT_gpr_key:' in workflow
bootstrap_idx = workflow.index('Bootstrap pinned Morphe toolchain')
release_idx = workflow.index('uses: cycjimmy/semantic-release-action@v6')
assert bootstrap_idx < release_idx
print("Morphe CI bootstrap/no-registry verification passed")
