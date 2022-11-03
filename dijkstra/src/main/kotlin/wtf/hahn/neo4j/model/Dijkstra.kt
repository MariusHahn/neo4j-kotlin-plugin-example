package wtf.hahn.neo4j.model

import org.neo4j.graphdb.Relationship

data class Info(val settled: Boolean, val distance: Long, val relationship: Relationship?)

class DijkstraHeap(private val heap: MutableMap<Long, Info> = mutableMapOf()) {

    fun setSettled(nodeId: Long) {
        if (!heap.containsKey(nodeId)) throw UnsupportedOperationException()
        val info = info(nodeId)
        heap[nodeId] = Info(true, info.distance, info.relationship)
    }


    fun setNodeDistance(nodeId: Long, distance: Long, previous: Relationship? = null) {
        heap[nodeId] = Info(false, distance, previous)
    }

    fun getClosestNotSettled(): Long? {
        data class DistanceNodeId(val nodeId: Long? = null, val distance: Long = Long.MAX_VALUE)

        var distanceNodeId = DistanceNodeId()
        for ((nodeId, info) in heap) {
            if (!(info.settled || info.distance >= distanceNodeId.distance)) {
                distanceNodeId = DistanceNodeId(nodeId = nodeId, distance = info.distance)
            }
        }
        return distanceNodeId.nodeId
    }

    fun getPath(endNodeId: Long): List<Relationship> {
        val ids = mutableListOf<Relationship>()
        var info = info(endNodeId)
        while (isNotStartNode(info)) {
            val relationshipToPrevious = info.relationship!!
            ids.add(relationshipToPrevious)
            info = info(relationshipToPrevious.startNodeId)
        }
        return ids
    }

    fun isSettled(nodeId: Long): Boolean = heap[nodeId]?.settled ?: false

    fun distance(nodeId: Long): Long = info(nodeId).distance

    private fun isNotStartNode(info: Info) = info.relationship != null

    private fun info(nodeId: Long): Info = heap[nodeId]!!
}
