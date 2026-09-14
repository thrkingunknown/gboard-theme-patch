# Dva.11 Gboard Midnight Red

A standalone Morphe patch that adds one isolated `Midnight Red` theme to full Gboard.
It does not replace Gboard's built-in themes and uses uniquely named assets to avoid
collisions with other patch bundles.

## Features

- Pitch-black AMOLED keyboard background by default.
- Pure-red action/Enter key by default.
- Dark red modifier/bordered-key tones by default.
- Four configurable colour options in Morphe:
  - Background colour
  - Primary colour
  - Secondary colour
  - Tertiary colour
- Accepts `#RRGGBB` or `#AARRGGBB` values.
- Package-level compatibility with any Gboard version; the patch fails closed when
  its theme-listing structure cannot be located.
- Does not overwrite Gboard's built-in Red/Pitch Black/System Auto/Dynamic Color themes.

## Critical crash/duplication fixes

The previous implementation had two independent runtime problems:

1. It used `Bundle` (`p1`) as if it were an Android `Context`, which is invalid for
   the theme resource loader.
2. It anchored insertion on `Ljxu.<init>`. That constructor is inside Gboard's
   theme-building loop, so the custom theme was appended repeatedly.
3. The constructor receiver was previously allocated as `Ljyj` and invoked as
   `Ljxq`, causing ART's `VerifyError`.

The current patch obtains a real `Context` from the fragment, allocates the
`Ljxq` receiver as `Ljxq`, and injects immediately before the method's final
instruction so the registration happens once after the built-in theme loop.

## Compatibility / coexistence

The patch only writes:

- `assets/theme/theme_package_metadata_midnight_red.binarypb`
- `assets/theme/style_sheet_color_mrd.binarypb`
- `assets/theme/style_sheet_color_mrd_border.binarypb`

No existing theme asset is replaced. The patch has no dependency on Morphe,
Adobo, or JasonWu Gboard patch bundles.

## Build

Use **GitHub Actions → Build Morphe Patch → Run workflow**.

The workflow performs source checks, builds the `.mpp`, validates its ZIP structure,
calculates SHA-256, and optionally publishes the exact `v1.0.0` release.

## Important verification limitation

A patch bundle can be compiled and structurally validated without proving that a
specific obfuscated Gboard build accepts the injected method. Final runtime
validation still requires applying the generated `.mpp` to a real Gboard APK and
opening **Settings → Theme**. The CI therefore treats fingerprint resolution and
APK/Dex verification as separate checks rather than claiming that a compile alone
proves runtime compatibility.
