plugins {
    id("java")
}

repositories {
    mavenCentral()
}

dependencies {
    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    // https://mvnrepository.com/artifact/org.jetbrains.kotlin/kotlin-stdlib
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${Versions.koltin}")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.koltin}")
    // Use the Kotlin test library.
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    // Use the Kotlin JUnit integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

