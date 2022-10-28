plugins {
    id("java")
}

repositories {
    mavenCentral()
}

dependencies {
    // https://mvnrepository.com/artifact/org.neo4j/neo4j
    compileOnly("org.neo4j:neo4j:${Versions.neo4j}")
    // https://mvnrepository.com/artifact/org.neo4j.test/neo4j-harness
    testImplementation("org.neo4j.test:neo4j-harness:${Versions.neo4j}")
    // https://mvnrepository.com/artifact/org.neo4j.driver/neo4j-java-driver
    testImplementation("org.neo4j.driver:neo4j-java-driver:${Versions.neo4j}")


}