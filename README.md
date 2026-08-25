# Gboard Midnight Red

## Exact target

- Package: `com.google.android.inputmethod.latin`
- Version: `18.0.3.954559732`
- Input: full release APKM

## What the patch does

Adds a separate `Midnight Red` theme package and inserts it into Gboard's theme
listing. It does not overwrite the built-in Red or Pitch Black themes.

## Important V4 fix

The previous repository contained a Dalvik verifier bug:

```smali
new-instance v12, Ljyj;
...
invoke-direct {v12, v9, v11}, Ljxq;-><init>(Ljava/lang/String;Ljyj;)V
```

`v12` was allocated as `Ljyj` and then passed as the receiver to the constructor
of `Ljxq`. Android correctly rejected the method with `VerifyError`.

V4 allocates the correct receiver type:

```smali
new-instance v12, Ljxq;
...
invoke-direct {v12, v9, v11}, Ljxq;-><init>(Ljava/lang/String;Ljyj;)V
```

The GitHub Actions workflow now contains a static verification step that fails
before Gradle if this mismatch is present.

## Build

GitHub Actions -> Build Morphe Patch -> Run workflow.

The workflow builds the `.mpp`, validates the archive, and uploads it as an
artifact.
