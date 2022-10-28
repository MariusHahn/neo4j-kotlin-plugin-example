import Versions.javaVersion
import java.nio.file.Files.isDirectory

plugins {
    id(Plugins.kotlin) version Versions.koltin
    id(Plugins.kotlinDependencies)
    id(Plugins.neo4jDependencies)
    id("java-library")
}

kotlin { jvmToolchain { languageVersion.set(javaVersion) } }
repositories { mavenCentral() }
dependencies {}




