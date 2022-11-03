import Versions.javaVersion

plugins {
    id(Plugins.kotlin) version Versions.koltin
    id(Plugins.kotlinDependencies)
    id(Plugins.neo4jDependencies)
    id("java-library")
}

kotlin { jvmToolchain { languageVersion.set(javaVersion) } }
repositories { mavenCentral() }

tasks.jar {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    configurations.implementation.get().isCanBeResolved = true
    val kotlinRuntime = configurations.implementation.get()
        .filter { it.name.endsWith("jar") }
        .filter { it.name.contains("kotlin") }
        .map{zipTree(it)}
    from(kotlinRuntime)
}

