# Gboard AMOLED Theme Studio

Standalone Morphe patch for **Gboard only** (`com.google.android.inputmethod.latin`).

## Default values

| Option | Default |
|---|---|
| Theme name | `Midnight Red` |
| Background | `#000000` |
| Primary / action | `#FF0000` |
| Secondary / normal keys | `#2A0A0A` |
| Tertiary / modifier keys | `#1F0B0B` |
| Additional themes | `(none)` |

Colours accept `#RRGGBB` or `#AARRGGBB`.

## Additional themes

The **Additional themes** option is functional.

Preferred format:
`Name|Background|Primary|Secondary|Tertiary`

Separate multiple themes with `;;` (new lines and single `;` are also accepted):
`Ocean|#000000|#00B7FF|#08202A|#123C4A;;Purple|#000000|#B56CFF|#24113A|#40205C`

The default Midnight Red theme is always generated from the five primary options.
Duplicate names are collapsed case-insensitively. Blank/trailing entries are ignored.
A name-only or partial entry uses the default palette for omitted colors. Invalid-color
entries are ignored instead of aborting the entire patch.

## Morphe source metadata

This patch bundle is usable as a local `.mpp` regardless of remote metadata. For a **Remote** Morphe source, the GitHub repository itself must be publicly reachable and the file below must exist on its `main` branch:

Repository:
`https://github.com/thrkingunknown/gboard-theme-patch`

Metadata:
`https://raw.githubusercontent.com/thrkingunknown/gboard-theme-patch/main/patches-bundle.json`

Morphe Manager resolves a GitHub repository source to `patches-bundle.json` on the repository's `main` branch. **Metadata N/A means Manager could not fetch that remote JSON; it is not an MPP patch-code failure.** citeturn883791search1turn883791search0

After the repository and release are actually published, remove the old `Metadata N/A` source entry and re-add either the repository URL above or the direct raw JSON URL. If the repository is private, use the `.mpp` as a Local source instead; a private GitHub repository cannot provide public remote metadata to Morphe Manager.

## Compatibility

- Package: `com.google.android.inputmethod.latin`
- App: Gboard
- Supported version: **Any**
- No `AppTarget` version restrictions.

Morphe documents that specifying a package without version targets means the patch
is compatible with any version of that package.

## Runtime safety

Theme registration is inserted after Gboard finishes constructing its built-in
theme list. It does not hook the constructor inside that loop.

The registration obtains a real Android `Context` from the Fragment and constructs
`Ljxq` with an `Ljxq` receiver, addressing the ART verifier failure from the
previous build.

## Version

Project/release version is managed by semantic-release; the generated release metadata below is authoritative.


<!-- PATCHES_START -->
> **[v1.0.0](https://github.com/thrkingunknown/gboard-theme-patch/releases/tag/v1.0.0)** • `main` • 1 patches total
<details open>
<summary>📦 Gboard • 1 patch</summary>
<br>
| 💊&nbsp;Patch | 📜&nbsp;Description | ⚙️&nbsp;Options |
|----------|----------------|-----------|
| [Gboard AMOLED Theme Studio](#gboard-amoled-theme-studio) | Adds configurable standalone AMOLED Gboard themes. Midnight Red is the default palette; additional themes can be defined in one patch. | • Theme name<br>• Background<br>• Primary / action<br>• Secondary / normal keys<br>• Tertiary / modifier keys<br>• Additional themes |
</details>

<!-- PATCHES_END -->
