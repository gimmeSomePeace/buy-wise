plugins {
    alias(libs.plugins.kotlin.jvm)
    `java-test-fixtures`
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

    testImplementation(libs.coroutines.test)
    testImplementation(libs.mockk)
    testImplementation(kotlin("test"))
    testImplementation(testFixtures(project(":domain")))

    testFixturesImplementation(testFixtures(project(":domain")))
}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}
