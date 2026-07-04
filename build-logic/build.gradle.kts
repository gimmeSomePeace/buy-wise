val detektPluginId = "buywise.detekt"

plugins {
    `kotlin-dsl`
}

group = "me.gimmesomepeace"
version = "unspecified"

repositories {
    gradlePluginPortal()
    mavenCentral()
}

gradlePlugin {
    plugins {
        register("DetektConventionPlugin") {
            id = detektPluginId
            implementationClass = "me.gimmesomepeace.buildlogic.DetektConventionPlugin"
        }
    }
}

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

dependencies {
    implementation(libs.findLibrary("kotlin-gradle-plugin").get())
    implementation(libs.findLibrary("detekt-gradle-plugin").get())
}

kotlin {
    jvmToolchain(21)
}
