plugins {
    alias(libs.plugins.kotlin.jvm)
    id("buywise.detekt")
    application
}

kotlin {
    jvmToolchain(21)
}

dependencies {}

application {
    mainClass = "me.gimmesomepeace.app.AppKt"
}
