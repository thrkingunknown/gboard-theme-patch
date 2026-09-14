from pathlib import Path

s = Path("patches/src/main/kotlin/dev/thrkingunknown/gboard/MidnightRedThemePatch.kt")
if not s.exists():
    s = Path("patches/src/main/kotlin/dev/dva11/gboard/MidnightRedThemePatch.kt")
text = s.read_text()
build = Path("patches/build.gradle.kts").read_text()

checks = [
    'val themeListing = Fingerprint(',
    'parameters = listOf("Landroid/os/Bundle;")',
    'themeListing.method.implementation?.instructions',
    'themeListing.method.addInstructions(',
    'import app.morphe.patcher.extensions.InstructionExtensions.addInstructions',
    'stringOption(key = "Background")',
    'stringOption(key = "Primary / action")',
    'stringOption(key = "Secondary / normal keys")',
    'stringOption(key = "Tertiary / modifier keys")',
    'stringOption(key = "Additional themes")',
    'Landroidx/fragment/app/Fragment;->getContext()Landroid/content/Context;',
    'new-instance v12, Ljxq;',
    'invoke-direct {v12, v10, v11}, Ljxq;-><init>(Ljava/lang/String;Ljyj;)V',
]
for item in checks:
    assert item in text, item

assert 'stringOption(name =' not in text
assert 'AppTarget(' not in text
assert 'Ljxu;' not in text
assert 'new-instance v12, Ljyj;' not in text
assert 'anchor.index' not in text
assert 'anchor.index + 1' not in text
assert 'name = "Gboard AMOLED Themes"' in build
assert 'contact = "https://github.com/thrkingunknown"' in build
print("Static patch/template verification passed")
