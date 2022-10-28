import org.apache.commons.lang3.tuple.MutablePair
import org.neo4j.graphdb.Relationship

data class Info(val settled: Boolean, val distance: Long, val relationship: Relationship?)

class DikstraHeap(private val heap: MutableMap<Long, Info> = mutableMapOf()) {

    fun setSettled(nodeId: Long) {
        if (!heap.containsKey(nodeId)) throw UnsupportedOperationException()
        val info = heap[nodeId]
        heap[nodeId] = Info(true, info!!.distance, info.relationship)
    }

    fun setNodeDistance(nodeId: Long, distance: Long, previous: Relationship?) {
        heap[nodeId] = Info(false, distance, previous)
    }

    fun isSettled(nodeId: Long): Boolean {
        return heap[nodeId] != null && heap[nodeId]!!.settled
    }

    fun getClosestNotSettled(): Long? {
        val distanceNodeId = MutablePair<Long, Long?>(Long.MAX_VALUE, null)
        heap.entries.stream()
            .filter { (_, value): Map.Entry<Long, Info> -> !value!!.settled }
            .forEach { (key, value): Map.Entry<Long, Info> ->
                val distance = value!!.distance
                if (distance < distanceNodeId.getLeft()) {
                    distanceNodeId.setLeft(distance)
                    distanceNodeId.setRight(key)
                }
            }
        return distanceNodeId.getRight()
    }

    fun getCurrentDistance(nodeId: Long): Long {
        return heap[nodeId]!!.distance
    }

    fun getPath(endNodeId: Long): List<Relationship> {
        val ids: MutableList<Relationship> = ArrayList()
        var info = heap[endNodeId]
        while (info!!.relationship != null) {
            ids.add(info.relationship!!)
            info = heap[info.relationship!!.startNodeId]
        }
        return ids
    }
}
