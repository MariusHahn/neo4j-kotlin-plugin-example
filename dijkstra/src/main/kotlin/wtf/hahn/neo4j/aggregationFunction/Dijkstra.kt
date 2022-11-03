package wtf.hahn.neo4j.aggregationFunction

import wtf.hahn.neo4j.model.DijkstraHeap
import org.neo4j.graphdb.*
import org.neo4j.procedure.Context
import org.neo4j.procedure.Name
import org.neo4j.procedure.UserFunction
import wtf.hahn.neo4j.model.ShortestPropertyPath
import wtf.hahn.neo4j.util.ReverseIterator


class Dijkstra {

    @Context lateinit var graphDatabaseService: GraphDatabaseService

    @UserFunction
    fun sourceTargetKotlin(
        @Name("startNode") startNode: Node,
        @Name("endNode") endNode: Node,
        @Name("type") type: String,
        @Name("propertyKey") propertyKey: String,
    ): Path {
        val relationshipType = RelationshipType.withName(type)
        val heap = DijkstraHeap()
        graphDatabaseService.beginTx().use { transaction: Transaction ->
            heap.setNodeDistance(startNode.id, 0L)
            while (heap.getClosestNotSettled() != null && !heap.isSettled(endNode.id)) {
                val toSettle = transaction.getNodeById(heap.getClosestNotSettled()!!)
                for (relationship: Relationship in toSettle.getRelationships(Direction.OUTGOING, relationshipType)) {
                    heap.setNodeDistance(
                        relationship.endNode.id,
                        relationship.getProperty(propertyKey) as Long + heap.distance(toSettle.id),
                        relationship
                    )
                }
                heap.setSettled(toSettle.id)
            }
            return ShortestPropertyPath(ReverseIterator(heap.getPath(endNode.id)), propertyKey)
        }
    }

}