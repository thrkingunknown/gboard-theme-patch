from pathlib import Path

settings = Path("settings.gradle.kts").read_text()
workflow = Path(".github/workflows/release.yml").read_text()

assert settings.startswith('rootProject.name = "gboard-amoled-themes"')
assert "\npluginManagement {" in settings
assert 'val localMorphePlugin = file(".gradle-deps/morphe-patches-gradle-plugin")' in settings
assert 'includeBuild(localMorphePlugin)' in settings
assert 'id("app.morphe.patches") version "1.3.3"' in settings
assert 'val localMorphePatcher = file(".gradle-deps/morphe-patcher")' in settings
assert 'substitute(module("app.morphe:morphe-patcher")).using(project(":"))' in settings

bootstrap = workflow.split("Bootstrap pinned Morphe build dependencies", 1)[1]
assert "clone_tag MorpheApp/morphe-patches-gradle-plugin v1.3.3 1.3.3" in bootstrap
assert "clone_tag MorpheApp/morphe-patcher v1.7.0 1.7.0" in bootstrap
assert "GIT_TERMINAL_PROMPT: '0'" in workflow

print("Settings/bootstrap verification passed")
