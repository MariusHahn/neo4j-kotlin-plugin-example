package wtf.hahn.neo4j.model

import org.neo4j.graphdb.Entity
import org.neo4j.graphdb.Node
import org.neo4j.graphdb.Path
import org.neo4j.graphdb.Relationship
import wtf.hahn.neo4j.util.ReverseIterator
import wtf.hahn.neo4j.util.ZipIterator

class ShortestPropertyPath(relationships: Iterator<Relationship>, propertyKey: String) : Path {

    private val length: Int
    private val nodes: List<Node>
    private val relationships: List<Relationship>

    init {
        this.relationships = materializeRelationships(relationships)
        length = materializeLength(propertyKey)
        nodes = materializeNodes()
    }

    override fun startNode(): Node {
        return relationships.first().startNode
    }

    override fun endNode(): Node {
        return relationships[relationships.size - 1].endNode
    }

    override fun lastRelationship(): Relationship {
        return relationships.last()
    }

    override fun relationships(): Iterable<Relationship> {
        return relationships
    }

    override fun reverseRelationships(): Iterable<Relationship> {
        return ReverseIterator(relationships)
    }

    override fun nodes(): Iterable<Node> {
        return nodes
    }

    override fun reverseNodes(): Iterable<Node> {
        return ReverseIterator(nodes)
    }

    override fun length(): Int {
        return length
    }

    override fun iterator(): MutableIterator<Entity> {
        return ZipIterator(nodes, relationships)
    }

    private fun materializeRelationships(relationships: Iterator<Relationship>): List<Relationship> {
        val relationshipList: MutableList<Relationship> = mutableListOf()
        relationships.forEach(relationshipList::add)
        return relationshipList
    }

    private fun materializeNodes(): List<Node> {
        if (relationships.isEmpty()) return listOf()
        val nodes = mutableListOf<Node>()
        nodes.add(relationships[0].startNode)
        for (relationship in relationships) {
            nodes.add(relationship.endNode)
        }
        return nodes
    }

    private fun materializeLength(propertyKey: String): Int {
        return relationships.stream()
            .map { relationship: Relationship -> relationship.getProperty(propertyKey) }
            .map { cost: Any -> (cost as? Long)?.toString() ?: cost as String }
            .mapToLong { s: String? -> java.lang.Long.valueOf(s) }
            .sum().toInt()
    }
}