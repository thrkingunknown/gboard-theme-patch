package dev.dva11.gboard

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.ApkFileType
import app.morphe.patcher.patch.Compatibility
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.rawResourcePatch
import app.morphe.patcher.patch.stringOption
import java.util.Base64

private const val PACKAGE_NAME = "com.google.android.inputmethod.latin"
private const val METADATA_PATH = "assets/theme/theme_package_metadata_midnight_red.binarypb"
private const val SHEET_PATH = "assets/theme/style_sheet_color_mrd.binarypb"
private const val BORDER_PATH = "assets/theme/style_sheet_color_mrd_border.binarypb"
private const val THEME_NAME = "Midnight Red"

/**
 * Deliberately declares no app version targets.  Morphe treats a package-level
 * compatibility declaration without targets as compatible with any version.
 * The bytecode fingerprint below still fails closed when the theme-listing
 * structure is not present, rather than producing a partially patched APK.
 */
private val compatibility = Compatibility(
    name = "Gboard",
    packageName = PACKAGE_NAME,
    apkFileType = ApkFileType.APKM,
    appIconColor = 0x4285F4,
)

private val defaultPaletteBytes: ByteArray by lazy {
    Base64.getDecoder().decode(
        "EjUKK2RlZmF1bHRfa2V5Ym9hcmRfYmFja2dyb3VuZF9zZWNvbmRhcnlfY29sb3ISBgiKlKj5DxIzCilkZWZhdWx0X2tleWJvYXJkX2JhY2tncm91bmRfcHJpbWFyeV9jb2xvchIGCICAgPgPEi4KJGRlZmF1bHRfZ2VuZXJpY19hY2NlbnRfY29sb3JfcHJlc3NlZBIGCICA2P4PEiYKHGRlZmF1bHRfZ2VuZXJpY19hY2NlbnRfY29sb3ISBgiAgPz/DxIsCiJkZWZhdWx0X2JvcmRlcmVkX2tleV9jb2xvcl9wcmVzc2VkEgYItu3a/Q8SJAoaZGVmYXVsdF9ib3JkZXJlZF9rZXlfY29sb3ISBgiu3Lj5DxIxCidkZWZhdWx0X2JvcmRlcmVkX2tleV9kYXJrX2NvbG9yX3ByZXNzZWQSBgit27b9DxIpCh9kZWZhdWx0X2JvcmRlcmVkX2tleV9kYXJrX2NvbG9yEgYIi5b8+A8SNgosY29sb3JfZ2VuZXJpY19leHRlbnNpb25fYmFja2dyb3VuZF9hY3RpdmF0ZWQSBgiKlKj5DxInCh1jb2xvcl9ib3R0b21faW5kaWNhdG9yX2FjdGl2ZRIGCICA/P8P"
    )
}

private val metadataBytes: ByteArray by lazy {
    Base64.getDecoder().decode(
        "EiFzdHlsZV9zaGVldF9jb2xvcl9jb21tb24uYmluYXJ5cGISJnN0eWxlX3NoZWV0X25vbl9keW5hbWljX2NvbG9yLmJpbmFyeXBiEiZzdHlsZV9zaGVldF9jb2xvcl9sYWJlbF93aGl0ZS5iaW5hcnlwYhIqc3R5bGVfc2hlZXRfY29sb3JfZXhwcmVzc2lvbl9kYXJrLmJpbmFyeXBiEiBzdHlsZV9zaGVldF9jb2xvcl9ydWxlcy5iaW5hcnlwYhIec3R5bGVfc2hlZXRfY29sb3JfbXJkLmJpbmFyeXBiGlIIARInc3R5bGVfc2hlZXRfY29sb3JfcnVsZXNfYm9yZGVyLmJpbmFyeXBiEiVzdHlsZV9zaGVldF9jb2xvcl9tcmRfYm9yZGVyLmJpbmFyeXBi"
    )
}

private val borderBytes = byteArrayOf()

private fun parseArgb(value: String, optionName: String): Int {
    val normalized = value.trim().removePrefix("#")
    require(normalized.matches(Regex("[0-9a-fA-F]{6}|[0-9a-fA-F]{8}"))) {
        "$optionName must be #RRGGBB or #AARRGGBB"
    }

    val argb = when (normalized.length) {
        6 -> "FF$normalized"
        else -> normalized
    }.toLong(16)

    return argb.toInt()
}

/**
 * Rewrites only the colour scalar values in the existing theme-sheet protobuf.
 * Keeping field names and wire structure byte-for-byte stable makes the resource
 * patch independent of generated resource IDs and avoids touching Gboard's
 * existing theme packages.
 */
private fun paletteBytes(
    background: Int,
    primary: Int,
    secondary: Int,
    tertiary: Int,
): ByteArray {
    val replacements = mapOf(
        "default_keyboard_background_secondary_color" to background,
        "default_keyboard_background_primary_color" to background,
        "default_generic_accent_color_pressed" to primary,
        "default_generic_accent_color" to primary,
        "default_bordered_key_color_pressed" to secondary,
        "default_bordered_key_color" to secondary,
        "default_bordered_key_dark_color_pressed" to tertiary,
        "default_bordered_key_dark_color" to tertiary,
        "color_generic_extension_background_activated" to secondary,
        "color_bottom_indicator_active" to primary,
    )

    val out = defaultPaletteBytes.copyOf()
    var cursor = 0
    for ((name, color) in replacements) {
        val needle = name.toByteArray(Charsets.UTF_8)
        val start = out.indexOfSubsequence(needle, cursor)
        require(start >= 0) { "Theme colour field missing: $name" }
        val valueStart = start + needle.size
        val fieldStart = out.indexOf(0x12.toByte(), valueStart)
        require(fieldStart >= 0) { "Theme colour value missing: $name" }
        // The embedded colour message is: 0x12 0x06 0x08 <uint32 varint>.
        val scalarStart = fieldStart + 3
        val encoded = encodeVarint(color.toLong() and 0xFFFFFFFFL)
        require(encoded.size == 5) { "Unexpected colour varint width for $name" }
        require(out[fieldStart + 1].toInt() == 6) { "Unexpected colour message size for $name" }
        require(out[scalarStart] == 0x08.toByte()) { "Unexpected colour scalar tag for $name" }
        System.arraycopy(encoded, 0, out, scalarStart + 1, encoded.size)
        cursor = scalarStart + 1 + encoded.size
    }
    return out
}

private fun ByteArray.indexOfSubsequence(needle: ByteArray, fromIndex: Int): Int {
    if (needle.isEmpty()) return fromIndex.coerceAtMost(size)
    outer@ for (i in fromIndex..size - needle.size) {
        for (j in needle.indices) {
            if (this[i + j] != needle[j]) continue@outer
        }
        return i
    }
    return -1
}

private fun encodeVarint(value: Long): ByteArray {
    var v = value
    val result = ByteArray(5)
    for (i in 0 until 5) {
        if ((v and 0xFFFFFF80L) == 0L) {
            result[i] = v.toByte()
            return result.copyOf(i + 1)
        }
        result[i] = ((v and 0x7F) or 0x80).toByte()
        v = v ushr 7
    }
    error("32-bit colour did not fit in a varint")
}

val midnightRedTheme = bytecodePatch(
    name = "Dva.11 Midnight Red Theme",
    description = "Adds one independent AMOLED Midnight Red Gboard theme with configurable background, primary, secondary, and tertiary colours.",
    default = false,
) {
    val backgroundColor by stringOption(
        name = "Background colour",
        default = "#000000",
    )
    val primaryColor by stringOption(
        name = "Primary colour",
        default = "#FF0000",
    )
    val secondaryColor by stringOption(
        name = "Secondary colour",
        default = "#120404",
    )
    val tertiaryColor by stringOption(
        name = "Tertiary colour",
        default = "#2A0A0A",
    )

    compatibleWith(compatibility)

    execute {
        val palette = paletteBytes(
            background = parseArgb(backgroundColor, "Background colour"),
            primary = parseArgb(primaryColor, "Primary colour"),
            secondary = parseArgb(secondaryColor, "Secondary colour"),
            tertiary = parseArgb(tertiaryColor, "Tertiary colour"),
        )

        // Resource-only mutation.  Unique asset names ensure this patch does not
        // overwrite or collide with Morphe/Adobo/JasonWu theme assets.
        get(SHEET_PATH).writeBytes(palette)
        get(BORDER_PATH).writeBytes(borderBytes)
        get(METADATA_PATH).writeBytes(metadataBytes)

        val themeListing = Fingerprint(
            definingClass = "Lcom/google/android/apps/inputmethod/libs/theme/listing/ThemeListingFragment;",
            parameters = listOf("Landroid/os/Bundle;"),
            returnType = "V",
        )

        // Inject immediately before the method's final instruction.  The previous
        // implementation anchored on Ljxu.<init>, which is inside Gboard's theme
        // construction loop; that caused the new theme to be appended once per
        // built-in theme and produced the visible duplicates.
        val instructions = themeListing.method.implementation?.instructions
            ?: error("Theme listing implementation not found")
        val insertionIndex = instructions.lastIndex
        require(insertionIndex >= 0) { "Theme listing method has no instructions" }

        themeListing.method.addInstructions(
            insertionIndex,
            """
                invoke-virtual {p0}, Landroidx/fragment/app/Fragment;->getContext()Landroid/content/Context;
                move-result-object v9
                const-string v10, "$METADATA_PATH"
                const/4 v11, 0x1
                new-instance v12, Lqyk;
                invoke-direct {v12, v10, v11}, Lqyk;-><init>(Ljava/lang/String;Z)V
                invoke-static {v9, v12}, Lqzd;->a(Landroid/content/Context;Lqyk;)Lqye;
                move-result-object v10
                invoke-interface {v10}, Lqye;->c()Lrdd;
                move-result-object v10
                invoke-static {v9, v12}, Ljyj;->e(Landroid/content/Context;Lqyk;)Ljyj;
                move-result-object v10
                const-string v9, "$THEME_NAME"
                new-instance v12, Ljxq;
                invoke-direct {v12, v9, v10}, Ljxq;-><init>(Ljava/lang/String;Ljyj;)V
                invoke-interface {v5, v12}, Ljava/util/List;->add(Ljava/lang/Object;)Z
            """.trimIndent(),
        )
    }
}
