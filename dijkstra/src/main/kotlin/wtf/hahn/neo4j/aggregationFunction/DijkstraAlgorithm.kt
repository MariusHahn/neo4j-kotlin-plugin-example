package wtf.hahn.neo4j.aggregationFunction

import DikstraHeap
import org.neo4j.graphdb.*
import org.neo4j.procedure.Context
import org.neo4j.procedure.Name
import org.neo4j.procedure.UserFunction
import wtf.hahn.neo4j.model.ShortestPropertyPath
import wtf.hahn.neo4j.util.ReverseIterator


class DijkstraAlgorithm {

    @Context lateinit var graphDatabaseService: GraphDatabaseService

    @UserFunction
    fun sourceTargetKotlin(
        @Name("startNode") startNode: Node,
        @Name("endNode") endNode: Node,
        @Name("type") type: String,
        @Name("propertyKey") propertyKey: String,
    ): Path {
        val relationshipType = RelationshipType.withName(type)
        val heap = DikstraHeap()
        var path: ShortestPropertyPath
        graphDatabaseService!!.beginTx().use { transaction ->
            heap.setNodeDistance(startNode.id, 0L, null)
            while (heap.getClosestNotSettled() != null && !heap.isSettled(endNode.id)) {
                val toSettle = transaction.getNodeById(heap.getClosestNotSettled()!!)
                toSettle.getRelationships(Direction.OUTGOING, relationshipType)
                    .forEach { relationship: Relationship ->
                        heap.setNodeDistance(
                            relationship.endNode.id,
                            relationship.getProperty(propertyKey) as Long +
                                    heap.getCurrentDistance(toSettle.id), relationship)
                    }

                heap.setSettled(toSettle.id)
            }
            val relationships =
                ReverseIterator(heap.getPath(endNode.id))
            path = ShortestPropertyPath(relationships, propertyKey)
            return path
        }
    }

}