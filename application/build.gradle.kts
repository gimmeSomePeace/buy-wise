plugins {
    alias(libs.plugins.kotlin.jvm)
    id("buywise.detekt")
}

repositories {
    mavenCentral()
}

dependencies {
    // Local module dependencies
    implementation(project(":domain"))

    // JUnit 5
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.platform.launcher)

    // AssertJ
    testImplementation(libs.assertj)
    testImplementation(testFixtures(project(":domain")))
    testImplementation(libs.coroutines.test)
}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}
