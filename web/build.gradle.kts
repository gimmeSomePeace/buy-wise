plugins {
    alias(libs.plugins.kotlin.jvm)
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":application"))

    implementation(platform(libs.spring.boot.dependencies))
    implementation(platform(libs.springdoc.openapi.bom))

    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.starter.validation)
    implementation(libs.kotlin.coroutines.reactor)
    implementation(libs.jackson.module.kotlin)
    implementation(libs.springdoc.openapi.starter.webmvc.ui)

    testImplementation(libs.coroutines.test)
    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.spring.boot.starter.webmvc.test)

    testImplementation(libs.spring.mockk)
    testRuntimeOnly(libs.junit.platform.launcher)

    testImplementation(testFixtures(project(":domain")))
    testImplementation(testFixtures(project(":application")))
}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}
