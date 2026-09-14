from pathlib import Path

s = Path("patches/src/main/kotlin/dev/dva11/gboard/MidnightRedThemePatch.kt").read_text()
b = Path("patches/build.gradle.kts").read_text()

required = [
    'definingClass = "Lcom/google/android/apps/inputmethod/libs/theme/listing/ThemeListingFragment;"',
    'parameters = listOf("Landroid/os/Bundle;")',
    'themeListing',
    'implementation?.instructions',
    'addInstructions(',
    'Landroidx/fragment/app/Fragment;->getContext()Landroid/content/Context;',
    'new-instance v12, Ljxq;',
    'invoke-direct {v12, v10, v11}, Ljxq;-><init>(Ljava/lang/String;Ljyj;)V',
    'stringOption(name = "Background',
    'Additional themes:',
]
for item in required:
    assert item in s, item

assert 'AppTarget(' not in s
assert 'Ljxu;' not in s
assert 'new-instance v12, Ljyj;' not in s
assert 'anchor.index' not in s
assert 'anchor.index + 1' not in s
assert 'contact = "https://github.com/thrkingunknown"' in b
assert 'name = "Gboard AMOLED Themes"' in b
print("Static verification passed")
