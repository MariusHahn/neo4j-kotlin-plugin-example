package wtf.hahn.neo4j.util

import org.neo4j.harness.Neo4j
import org.neo4j.harness.Neo4jBuilders
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.neo4j.driver.Config
import org.neo4j.driver.Driver
import org.neo4j.driver.GraphDatabase
import java.nio.file.Path
import java.nio.file.Paths

@TestInstance(PER_CLASS)
open class IntegrationTest(
    aggregationFunctions: Collection<Class<*>?>,
    procedures: Collection<Class<*>?>,
    functions: Collection<Class<*>?>,
    dataset: Dataset,
) {

    private val config: Config = Config.builder().withoutEncryption().build()
    private val neo4j: Neo4j

    init {
        val neo4jBuilder = Neo4jBuilders.newInProcessBuilder()
        aggregationFunctions.forEach(neo4jBuilder::withAggregationFunction)
        procedures.forEach(neo4jBuilder::withProcedure)
        functions.forEach(neo4jBuilder::withFunction)
        neo4j = neo4jBuilder.withDisabledServer().withFixture(dataset.cypher()).build()
    }

    fun driver() : Driver = GraphDatabase.driver(neo4j.boltURI(), config)

    @AfterAll
    fun stopNeo4j() {
        neo4j.close()
    }

    enum class Dataset(private val cypherFileName: String,
                       private val resources: Path = Paths.get("src", "test", "resources")
    ) {
        DIJKSTRA_SOURCE_TARGET_SAMPLE("neo4j_dijkstra_source_target_sample.cql");

        fun cypher(): Path = resources.resolve(cypherFileName)
    }
}
