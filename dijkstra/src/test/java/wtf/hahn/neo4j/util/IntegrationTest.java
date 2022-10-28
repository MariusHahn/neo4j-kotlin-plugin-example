package wtf.hahn.neo4j.util;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.TestInstance;
import org.neo4j.driver.Config;
import org.neo4j.harness.Neo4j;
import org.neo4j.harness.Neo4jBuilder;
import org.neo4j.harness.Neo4jBuilders;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;


@TestInstance(PER_CLASS)
public  class IntegrationTest {
    protected final Config build;
    protected final Neo4j neo4j;
    protected final URI uri;

    public IntegrationTest(Collection<Class> aggregationFunctions, Collection<Class> procedures, Collection<Class> functions, Dataset dataset) {
        build = Config.builder().withoutEncryption().build();
        Neo4jBuilder neo4jBuilder = Neo4jBuilders.newInProcessBuilder();
        aggregationFunctions.forEach(neo4jBuilder::withAggregationFunction);
        procedures.forEach(neo4jBuilder::withProcedure);
        functions.forEach(neo4jBuilder::withFunction);
        neo4j = neo4jBuilder.withDisabledServer().withFixture(dataset.getCypher()).build();
        uri = neo4j.boltURI();
    }

    @AfterAll
    public void stopNeo4j() {
        neo4j.close();
    }

    public enum Dataset{

        SABINE_ROUTE_PLANNING("sabine_route_planning.cql"),
        DIJKSTRA_SOURCE_TARGET_SAMPLE("neo4j_dijkstra_source_target_sample.cql");

        private final Path path;

        Dataset(String cypherFileName) {
            this.path = Paths.get("src", "test", "resources", cypherFileName);
        }

        public Path getCypher() {
            return path;
        }
    }
}
