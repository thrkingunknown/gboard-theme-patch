# 👋 Gboard AMOLED Themes — Morphe Patches

Configurable AMOLED theme patches for Gboard.

## Morphe Manager

Add the GitHub repository as a Morphe patch source:

https://morphe.software/add-source?github=thrkingunknown/gboard-theme-patch

Morphe Manager consumes `patches-bundle.json` from the repository default branch
and the `.mpp` published by the standard Morphe patch release workflow.

## Development

Use the `dev` branch for development. The repository follows the current
Morphe Patches template release model: semantic commit messages drive releases,
`dev` generates pre-releases, and merging `dev` into `main` produces a stable
release.

Build locally with:

```bash
./gradlew buildAndroid
```

The generated patch bundle is under:

```text
patches/build/libs/patches-*.mpp
```

## Theme

Default theme: **Midnight Red AMOLED**

- Background: `#000000`
- Primary/action: `#FF0000`
- Secondary: `#2A0A0A`
- Tertiary: `#1F0B0B`

The patch also exposes configurable colour options and supports additional
theme definitions.

## Release model

This repository intentionally uses the official Morphe template semantic-release
workflow rather than a custom release builder. Semantic-release generates
`patches-list.json` and `patches-bundle.json`, publishes the `.mpp`, and the
release workflow ensures the release metadata remains reachable from the active
release branch so Morphe Manager can read it. The root metadata files included
here are a bootstrap copy of the current `v1.1.0` release and will be replaced
by semantic-release on the next release.

Do not manually edit generated release files during normal development.

## 🩹 Patches

<!-- PATCHES_START -->
<!-- PATCHES_END -->
