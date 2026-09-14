from pathlib import Path
import base64
import hashlib
import re

ROOT = Path(__file__).resolve().parents[1]
PATCH = ROOT / 'patches/src/main/kotlin/dev/dva11/gboard/MidnightRedThemePatch.kt'
s = PATCH.read_text(encoding='utf-8')

assert 'AppTarget(' not in s
assert 'import app.morphe.patcher.patch.AppTarget' not in s
assert 'packageName = PACKAGE_NAME' in s
assert 'new-instance v12, Ljxq;' in s
assert 'new-instance v12, Ljyj;' not in s
assert 'invoke-static {p1' not in s
assert 'definingClass = "Ljxu;"' not in s
assert 'val insertionIndex = instructions.lastIndex' in s
assert 'invoke-virtual {p0}, Landroidx/fragment/app/Fragment;->getContext()Landroid/content/Context;' in s
assert s.count('invoke-interface {v5, v12}, Ljava/util/List;->add(Ljava/lang/Object;)Z') == 1

m = re.search(r'Base64\.getDecoder\(\)\.decode\(\s*"([^"]+)"', s)
assert m
palette = base64.b64decode(m.group(1))
assert len(palette) == 471
for field in (
    'default_keyboard_background_secondary_color',
    'default_keyboard_background_primary_color',
    'default_generic_accent_color_pressed',
    'default_generic_accent_color',
    'default_bordered_key_color_pressed',
    'default_bordered_key_color',
    'default_bordered_key_dark_color_pressed',
    'default_bordered_key_dark_color',
    'color_generic_extension_background_activated',
    'color_bottom_indicator_active',
):
    assert field.encode() in palette

print('PASS: source invariants')
print('PASS: palette structure')
print('palette-sha256:', hashlib.sha256(palette).hexdigest())
