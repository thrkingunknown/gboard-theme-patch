from pathlib import Path

s = Path('settings.gradle.kts').read_text()
assert 'id("app.morphe.patches") version "1.3.4"' in s
assert 'maven.pkg.github.com/MorpheApp/registry' in s
assert 'providers.environmentVariable("GPR_USER")' in s
assert 'providers.environmentVariable("GPR_KEY")' in s
assert 'getOrElse(System.getenv("GITHUB_ACTOR"))' in s
assert 'getOrElse(System.getenv("GITHUB_TOKEN"))' in s
assert 'mavenLocal()' in s
print('Gradle settings verification passed')
