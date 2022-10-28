package wtf.hahn.neo4j.aggregationFunction

import org.junit.Test
import org.neo4j.driver.GraphDatabase
import org.neo4j.driver.Session
import wtf.hahn.neo4j.util.IntegrationTest

class HelloWorldTest : IntegrationTest(listOf(), listOf(), listOf(
    DijkstraAlgorithm::class.java, HelloWorld::class.java), Dataset.DIJKSTRA_SOURCE_TARGET_SAMPLE) {

    @Test
    fun cypherTest() {
        GraphDatabase.driver(uri, build).use { driver ->
            val session: Session = driver.session()
            val cypher = """RETURN wtf.hahn.neo4j.aggregationFunction.helloWord("bla")"""
            val result = session.run(cypher)
            result.single().fields().forEach(::println)
        }
    }
}