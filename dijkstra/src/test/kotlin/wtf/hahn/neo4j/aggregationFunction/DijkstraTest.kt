package wtf.hahn.neo4j.aggregationFunction

import org.junit.Test
import org.neo4j.driver.GraphDatabase
import org.neo4j.driver.Session
import wtf.hahn.neo4j.util.IntegrationTest

class DijkstraTest : IntegrationTest(listOf(), listOf(), listOf(
    DijkstraAlgorithm::class.java), Dataset.DIJKSTRA_SOURCE_TARGET_SAMPLE) {

    @Test
    fun cypherTest() {
        GraphDatabase.driver(uri, build).use { driver ->
            val session: Session = driver.session()
            val cypher = """MATCH 
                           (a:Location {name: 'A'}),
                           (b:Location {name: 'F'}) 
                          WITH wtf.hahn.neo4j.aggregationFunction.sourceTargetKotlin(a, b, 'ROAD', 'cost') as path 
                          RETURN length(path) //, relationships(path) 
                         """
            val result = session.run(cypher)
            result.single().fields().forEach(::println)
        }
    }

}