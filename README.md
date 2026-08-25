# Gboard Midnight Red Theme Patch

## Target
- Package: `com.google.android.inputmethod.latin`
- Tested version: `18.0.3.954559732`
- Mutation: `assets/theme/style_sheet_color_red.binarypb`

This patch replaces the built-in red Colours preset with the Midnight Red AMOLED palette.

## Build
Use **Actions → Build Morphe Patch → Run workflow**.

The workflow uses the current Morphe template structure, Gradle version catalog, and
Morphe patch plugin `1.3.4`, then runs:

```text
gradle buildAndroid
```

The generated `.mpp` is uploaded as the workflow artifact.

## Authentication
The workflow uses the repository's `GITHUB_TOKEN` with `packages: read`.
If GitHub Packages authentication is denied for the Morphe registry, add a repository
secret containing a PAT with `read:packages` and update the workflow to use that secret.

Do not commit personal tokens.
