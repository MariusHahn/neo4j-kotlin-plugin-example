import org.gradle.jvm.toolchain.JavaLanguageVersion

object Versions {
    const val koltin = "1.7.20"
    const val neo4j = "4.4.9"
    val javaVersion = JavaLanguageVersion.of(11)
}

object Plugins {
    const val kotlin = "org.jetbrains.kotlin.jvm"
    const val kotlinDependencies = "kotlin-dependencies"
    const val neo4jDependencies = "neo4j-dependencies"
}
