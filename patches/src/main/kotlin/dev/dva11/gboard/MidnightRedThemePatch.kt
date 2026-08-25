package dev.dva11.gboard

import app.morphe.patcher.patch.rawResourcePatch
import java.util.Base64

private const val PACKAGE_NAME = "com.google.android.inputmethod.latin"
private const val TARGET_VERSION = "18.0.3.954559732"
private const val TARGET_PATH = "assets/theme/style_sheet_color_red.binarypb"

private val midnightRedSheetBytes: ByteArray by lazy {
    Base64.getDecoder().decode(
        "EjUKK2RlZmF1bHRfa2V5Ym9hcmRfYmFja2dyb3VuZF9zZWNvbmRhcnlfY29sb3ISBgiKlKj5DxIzCilkZWZhdWx0X2tleWJvYXJkX2JhY2tncm91bmRfcHJpbWFyeV9jb2xvchIGCICAgPgPEi4KJGRlZmF1bHRfZ2VuZXJpY19hY2NlbnRfY29sb3JfcHJlc3NlZBIGCICA2P4PEiYKHGRlZmF1bHRfZ2VuZXJpY19hY2NlbnRfY29sb3ISBgiAgPz/DxIsCiJkZWZhdWx0X2JvcmRlcmVkX2tleV9jb2xvcl9wcmVzc2VkEgYItu3a/Q8SJAoaZGVmYXVsdF9ib3JkZXJlZF9rZXlfY29sb3ISBgiu3Lj5DxIxCidkZWZhdWx0X2JvcmRlcmVkX2tleV9kYXJrX2NvbG9yX3ByZXNzZWQSBgit27b9DxIpCh9kZWZhdWx0X2JvcmRlcmVkX2tleV9kYXJrX2NvbG9yEgYIi5b8+A8SNgosY29sb3JfZ2VuZXJpY19leHRlbnNpb25fYmFja2dyb3VuZF9hY3RpdmF0ZWQSBgiKlKj5DxInCh1jb2xvcl9ib3R0b21faW5kaWNhdG9yX2FjdGl2ZRIGCICA/P8P"
    )
}

@Suppress("unused")
val midnightRedThemePatch = rawResourcePatch(
    name = "Midnight Red Theme",
    description = "Transforms Gboard's built-in red Colours preset into a pitch-black AMOLED theme with red-tinted modifier keys and a pure-red action key.",
    default = false,
) {
    compatibleWith(PACKAGE_NAME(TARGET_VERSION))

    execute {
        val themeSheet = get(TARGET_PATH)

        check(themeSheet.exists()) {
            "Required Gboard theme asset is missing: $TARGET_PATH"
        }

        themeSheet.writeBytes(midnightRedSheetBytes)
    }
}
