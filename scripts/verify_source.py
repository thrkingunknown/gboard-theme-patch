from pathlib import Path
import json
ROOT=Path(__file__).resolve().parents[1]
s=(ROOT/"patches/src/main/kotlin/dev/dva11/gboard/MidnightRedThemePatch.kt").read_text()
assert 'name = "Gboard AMOLED Theme Studio"' in s
assert 'packageName = PACKAGE_NAME' in s
assert 'AppTarget(' not in s
assert 'getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;' in s
assert 'Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;' in s
assert 'Landroidx/fragment/app/Fragment;->getContext()' not in s
assert 'new-instance v12, Ljxq;' in s
assert 'new-instance v12, Ljyj;' not in s
assert 'definingClass = "Ljxu;"' not in s
assert 'val endIndex = instructions.lastIndex' in s
assert 'split(";;")' in s
assert 'Additional theme #' in s
for x in ['DEFAULT_THEME_NAME','DEFAULT_BACKGROUND','DEFAULT_PRIMARY','DEFAULT_SECONDARY','DEFAULT_TERTIARY']:
    assert f'default = {x}' in s
assert 'default = "(none)"' in s
m=json.loads((ROOT/"verification-manifest.json").read_text())
props=(ROOT/"gradle.properties").read_text()
version=next(line.split("=",1)[1].strip() for line in props.splitlines() if line.startswith("version="))
assert m["version"]==version
assert m["package"]=="com.google.android.inputmethod.latin" and m["supportedVersion"]=="Any"
print("PASS: source invariants")
