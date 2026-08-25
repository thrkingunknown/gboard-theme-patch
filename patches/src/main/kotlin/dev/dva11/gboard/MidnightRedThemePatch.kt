package dev.dva11.gboard

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.methodCall
import app.morphe.patcher.patch.ApkFileType
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.rawResourcePatch
import java.util.Base64

private val compatibility = Compatibility(
    name = "Gboard",
    packageName = "com.google.android.inputmethod.latin",
    apkFileType = ApkFileType.APKM,
    appIconColor = 0x4285F4,
    targets = listOf(
        AppTarget(
            version = "18.0.3.954559732",
            description = "Full Gboard 18.0.3.954559732 release APKM"
        )
    )
)

private val paletteBytes by lazy {
    Base64.getDecoder().decode("EjUKK2RlZmF1bHRfa2V5Ym9hcmRfYmFja2dyb3VuZF9zZWNvbmRhcnlfY29sb3ISBgiKlKj5DxIzCilkZWZhdWx0X2tleWJvYXJkX2JhY2tncm91bmRfcHJpbWFyeV9jb2xvchIGCICAgPgPEi4KJGRlZmF1bHRfZ2VuZXJpY19hY2NlbnRfY29sb3JfcHJlc3NlZBIGCICA2P4PEiYKHGRlZmF1bHRfZ2VuZXJpY19hY2NlbnRfY29sb3ISBgiAgPz/DxIsCiJkZWZhdWx0X2JvcmRlcmVkX2tleV9jb2xvcl9wcmVzc2VkEgYItu3a/Q8SJAoaZGVmYXVsdF9ib3JkZXJlZF9rZXlfY29sb3ISBgiu3Lj5DxIxCidkZWZhdWx0X2JvcmRlcmVkX2tleV9kYXJrX2NvbG9yX3ByZXNzZWQSBgit27b9DxIpCh9kZWZhdWx0X2JvcmRlcmVkX2tleV9kYXJrX2NvbG9yEgYIi5b8+A8SNgosY29sb3JfZ2VuZXJpY19leHRlbnNpb25fYmFja2dyb3VuZF9hY3RpdmF0ZWQSBgiKlKj5DxInCh1jb2xvcl9ib3R0b21faW5kaWNhdG9yX2FjdGl2ZRIGCICA/P8P")
}
private val metadataBytes by lazy {
    Base64.getDecoder().decode("EiFzdHlsZV9zaGVldF9jb2xvcl9jb21tb24uYmluYXJ5cGISJnN0eWxlX3NoZWV0X25vbl9keW5hbWljX2NvbG9yLmJpbmFyeXBiEiZzdHlsZV9zaGVldF9jb2xvcl9sYWJlbF93aGl0ZS5iaW5hcnlwYhIqc3R5bGVfc2hlZXRfY29sb3JfZXhwcmVzc2lvbl9kYXJrLmJpbmFyeXBiEiBzdHlsZV9zaGVldF9jb2xvcl9ydWxlcy5iaW5hcnlwYhIec3R5bGVfc2hlZXRfY29sb3JfbXJkLmJpbmFyeXBiGlIIARInc3R5bGVfc2hlZXRfY29sb3JfcnVsZXNfYm9yZGVyLmJpbmFyeXBiEiVzdHlsZV9zaGVldF9jb2xvcl9tcmRfYm9yZGVyLmJpbmFyeXBi")
}
private val borderBytes by lazy {
    Base64.getDecoder().decode("")
}

private val midnightRedResources = rawResourcePatch(
    name = "Midnight Red Theme Resources",
    description = "Adds an independent Gboard Midnight Red theme package.",
    default = false,
) {
    compatibleWith(compatibility)
    execute {
        get("assets/theme/style_sheet_color_mrd.binarypb").writeBytes(paletteBytes)
        get("assets/theme/style_sheet_color_mrd_border.binarypb").writeBytes(borderBytes)
        get("assets/theme/theme_package_metadata_midnight_red.binarypb").writeBytes(metadataBytes)
    }
}

val midnightRedTheme = bytecodePatch(
    name = "Add Midnight Red Theme",
    description = "Adds a separate AMOLED black Gboard theme with a red action key.",
    default = false,
) {
    compatibleWith(compatibility)
    dependsOn(midnightRedResources)

    execute {
        val themeListing = Fingerprint(
            definingClass = "Lcom/google/android/apps/inputmethod/libs/theme/listing/ThemeListingFragment;",
            name = "f",
            parameters = listOf("Landroid/os/Bundle;"),
            returnType = "V",
            filters = listOf(
                methodCall(
                    definingClass = "Ljxu;",
                    name = "<init>",
                    returnType = "V",
                )
            ),
        )

        val anchor = themeListing.instructionMatches.lastOrNull()
            ?: error("Midnight Red insertion anchor not found")

        themeListing.method.addInstructions(
            anchor.index,
            """
                const-string v9, "assets:theme_package_metadata_midnight_red.binarypb"
                const/4 v10, 0x1
                new-instance v11, Lqyk;
                invoke-direct {v11, v9, v10}, Lqyk;-><init>(Ljava/lang/String;Z)V
                invoke-static {p1, v11}, Lqzd;->a(Landroid/content/Context;Lqyk;)Lqye;
                move-result-object v10
                invoke-interface {v10}, Lqye;->c()Lrdd;
                move-result-object v10
                const-string v9, "Midnight Red"
                new-instance v12, Ljxq;
                invoke-static {p1, v11}, Ljyj;->e(Landroid/content/Context;Lqyk;)Ljyj;
                move-result-object v11
                invoke-direct {v12, v9, v11}, Ljxq;-><init>(Ljava/lang/String;Ljyj;)V
                invoke-interface {v5, v12}, Ljava/util/List;->add(Ljava/lang/Object;)Z
            """.trimIndent(),
        )
    }
}
