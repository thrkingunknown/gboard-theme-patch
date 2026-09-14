from pathlib import Path

workflow = Path(".github/workflows/release.yml").read_text()
bootstrap = Path(".github/scripts/bootstrap_morphe.sh").read_text()

assert "GITHUB_ACTOR: ${{ github.actor }}" in workflow
assert "GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}" in workflow
assert "ORG_GRADLE_PROJECT_gpr_user: ${{ github.actor }}" in workflow
assert "ORG_GRADLE_PROJECT_gpr_key: ${{ secrets.GITHUB_TOKEN }}" in workflow
assert 'test -n "${ORG_GRADLE_PROJECT_gpr_user:-}"' in bootstrap
assert 'test -n "${ORG_GRADLE_PROJECT_gpr_key:-}"' in bootstrap
print("Morphe Gradle credentials propagation verification passed")
