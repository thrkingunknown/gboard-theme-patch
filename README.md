# Gboard Midnight Red Theme Patch

## Target

- Package: `com.google.android.inputmethod.latin`
- Version: `18.0.3.954559732`
- Input format tested: APKMirror `.apkm`
- Mutation: exactly one raw asset:
  `assets/theme/style_sheet_color_red.binarypb`

## What it does

It replaces the existing built-in red **Colours** preset. No new theme registry entry is injected.

Result:

- pitch-black keyboard background
- dark gray normal keys
- subtly red-tinted modifier roles
- pure-red action/Enter key
- no DEX mutation
- no manifest mutation
- no package rename
- no shared patch dependencies

## Patch isolation

This patch only writes one theme asset. It does not depend on or mutate the code paths used by:

- Jason Wu's Gboard patches
- Adobo's Gboard patches
- kveld9's Gboard Lite AMOLED patch

It is therefore isolated at the resource level. Another patch that also writes
`assets/theme/style_sheet_color_red.binarypb` would be a direct resource conflict.

## Resource payload

SHA-256 of replacement asset:

`c655225ce8aa342bff08c1f55d67527da827045761054b9acd002ab279e2e62d`

## Build

The source uses the same Morphe patch plugin family and raw-resource patch API used by
kveld9's current AMOLED implementation. The current kveld9 source uses
`rawResourcePatch`, `get(path)`, `exists()`, and `writeBytes()` for Gboard theme
binarypb assets.

Prerequisites:

- JDK 21
- Gradle wrapper from a current Morphe patches template/repository
- GitHub Packages credentials with `read:packages` if required by the current plugin

Set either:

```text
GITHUB_ACTOR=<github-user>
GITHUB_TOKEN=<token-with-read-packages>
```

or Gradle properties:

```text
gpr.user=<github-user>
gpr.key=<token-with-read-packages>
```

Then run:

```text
./gradlew :patches:buildAndroid
```

Expected output:

```text
patches/build/libs/patches-1.0.0.mpp
```

## Validation notes

The patch is intentionally version-pinned. It fails rather than silently doing nothing
when the target asset path is absent. It does not claim universal compatibility with
future Gboard releases.

The replacement binary payload was validated against the clean uploaded Gboard
18.0.3.954559732 package before this source bundle was generated.


## GitHub Actions build

This repository includes a manual GitHub Actions workflow:

`Actions → Build Morphe Patch → Run workflow`

The workflow:

1. checks out the repository;
2. uses Temurin JDK 21;
3. obtains Gradle 8.14;
4. authenticates to the Morphe GitHub Packages registry using the repository `GITHUB_TOKEN`;
5. runs `gradle :patches:buildAndroid`;
6. verifies that exactly one `.mpp` was produced;
7. uploads the `.mpp` and `SHA256SUMS.txt` as the workflow artifact.

If the repository's `GITHUB_TOKEN` is denied package-read access by GitHub for any reason,
create a repository secret named `MORPHE_PACKAGES_TOKEN` containing a classic GitHub PAT with
`read:packages`, then change the workflow's `GITHUB_TOKEN` environment assignment to:

```yaml
GITHUB_TOKEN: ${{ secrets.MORPHE_PACKAGES_TOKEN }}
```

Do not hardcode a personal token into the repository.
