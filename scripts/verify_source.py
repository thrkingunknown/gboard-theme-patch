from pathlib import Path

s = Path("patches/src/main/kotlin/dev/dva11/gboard/MidnightRedThemePatch.kt").read_text()
b = Path("patches/build.gradle.kts").read_text()

required = [
    'definingClass = "Lcom/google/android/apps/inputmethod/libs/theme/listing/ThemeListingFragment;"',
    'parameters = listOf("Landroid/os/Bundle;")',
    'val themeListing = Fingerprint(',
    'themeListing.method.implementation?.instructions',
    'themeListing.method.addInstructions(',
    'import app.morphe.patcher.extensions.InstructionExtensions.addInstructions',
    'Landroidx/fragment/app/Fragment;->getContext()Landroid/content/Context;',
    'new-instance v12, Ljxq;',
    'invoke-direct {v12, v10, v11}, Ljxq;-><init>(Ljava/lang/String;Ljyj;)V',
    'stringOption(key = "Background")',
    'stringOption(key = "Primary / action")',
    'stringOption(key = "Secondary / normal keys")',
    'stringOption(key = "Tertiary / modifier keys")',
    'stringOption(key = "Additional themes")',
    'additionalThemes ?: ""',
    'background ?: DEFAULT_BACKGROUND',
    'primary ?: DEFAULT_PRIMARY',
    'secondary ?: DEFAULT_SECONDARY',
    'tertiary ?: DEFAULT_TERTIARY',
]
for item in required:
    assert item in s, item

assert 'stringOption(name =' not in s
assert 'AppTarget(' not in s
assert 'Ljxu;' not in s
assert 'new-instance v12, Ljyj;' not in s
assert 'anchor.index' not in s
assert 'anchor.index + 1' not in s
assert 'contact = "https://github.com/thrkingunknown"' in b
assert 'name = "Gboard AMOLED Themes"' in b

print("Static source verification passed")
