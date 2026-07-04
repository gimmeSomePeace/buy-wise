
plugins {
    alias(libs.plugins.spotless)
}

spotless {
    kotlin {
        target("**/*.kt")
        ktlint()
    }

    kotlinGradle {
        target("**/*.gradle.kts")
        ktlint()
    }
}
