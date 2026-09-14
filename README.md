# Gboard AMOLED Themes

Morphe patch source for configurable Gboard AMOLED themes.

## Patch

**Midnight Red AMOLED Theme**

Default palette:

- Background: `#000000`
- Primary / action: `#FF0000`
- Secondary / normal keys: `#2A0A0A`
- Tertiary / modifier keys: `#1F0B0B`

The patch declares Gboard package compatibility without a version target, so Morphe
reports the supported Gboard version as **Any**. The implementation was developed
against the supplied Gboard `18.0.3.954559732` APKM.

## Morphe customization

The patch exposes options directly in Morphe:

- Theme name
- Background colour
- Primary/action colour
- Secondary/normal-key colour
- Tertiary/modifier-key colour
- Additional themes

Each colour accepts `#RRGGBB`, `#AARRGGBB`, or `Material You`.

`Material You` uses Gboard's native dynamic-colour dark theme as the base; the
requested role is left to Gboard's dynamic palette while other roles can remain
custom.

### Multiple themes

Enter one theme per line:

```text
Crimson|#000000|#FF1744|#22070B|#3A0C12
Cyber Red|#000000|#FF0033|#180008|Material You
```

Format:

```text
Name|Background|Primary|Secondary|Tertiary
```

Duplicate names are collapsed.

## Theme registration

The patch inserts registrations immediately before the terminal instruction of
`ThemeListingFragment.f(Bundle)`. It does not anchor to Gboard's existing theme
constructor loop, which was the source of the previous duplicate-theme behavior.

The injected code obtains a real `Context` through `Fragment.getContext()` and
allocates the `Ljxq` receiver with the correct type before invoking its constructor.

Theme metadata/assets are generated at patch time and use unique `mrd_*` filenames;
the built-in Gboard themes are not overwritten.

## Build / release

Use:

**Actions -> Build Morphe Patch -> Run workflow**

The manual workflow provides:

- **Publish to releases** — create a GitHub Release after a successful build.
- **Version** — enter `1.0.0`, `1.0.0-beta.1`, etc.
- **Release type** — `release` or `pre-release`.

The `.mpp` and SHA-256 checksum are always uploaded as workflow artifacts.

The workflow uses the current Morphe template action family: checkout 7, setup-java 6,
setup-gradle 6, upload-artifact 7, JDK 21, and Gradle 8.14.4.
