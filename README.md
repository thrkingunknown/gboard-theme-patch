# Gboard Midnight Red

Exact target:

- com.google.android.inputmethod.latin
- 18.0.3.954559732
- full release APKM

This version adds a separate Midnight Red theme entry. It does not overwrite the
built-in Red or Pitch Black theme resources.

The full Gboard target uses a different theme-listing implementation from the
Lite build used by kveld9. This patch uses the exact full-target classes and
native custom-theme construction path verified in the supplied APKM.

Build with GitHub Actions: Actions -> Build Morphe Patch -> Run workflow.

Insertion boundary is the final native Ljxu constructor after the custom-theme enumeration loop; the patch is therefore not executed conditionally inside that loop.
