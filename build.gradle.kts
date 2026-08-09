
plugins {
    alias(libs.plugins.spotless)
}

spotless {
    kotlin {
        println(
            "EditorConfig: ${rootProject.file(".editorconfig").absolutePath}",
        )
        println("Exists: ${rootProject.file(".editorconfig").exists()}")
        target("**/*.kt")
        ktlint("1.8.0")
            .setEditorConfigPath("${rootProject.projectDir}/.editorconfig")
//            .editorConfigOverride(
//                mapOf(
//                    "max_line_length" to "120",
//                ),
//            )
    }

    kotlinGradle {
        target("**/*.gradle.kts")
        ktlint("1.8.0")
            .setEditorConfigPath(rootProject.file(".editorconfig").absolutePath)
    }
}
