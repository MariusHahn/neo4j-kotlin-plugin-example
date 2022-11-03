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

    override fun startNode(): Node = relationships.first().startNode

    override fun endNode(): Node = relationships[relationships.size - 1].endNode

    override fun lastRelationship(): Relationship = relationships.last()

    override fun relationships(): Iterable<Relationship> = relationships

    override fun reverseRelationships(): Iterable<Relationship> = ReverseIterator(relationships)

    override fun nodes(): Iterable<Node> = nodes

    override fun reverseNodes(): Iterable<Node> = ReverseIterator(nodes)

    override fun length(): Int = length

    override fun iterator(): MutableIterator<Entity> = ZipIterator(nodes, relationships)

    private fun materializeRelationships(relationships: Iterator<Relationship>): List<Relationship> {
        val relationshipList = mutableListOf<Relationship>()
        relationships.forEach(relationshipList::add)
        return relationshipList
    }

    private fun materializeNodes(): List<Node> {
        if (relationships.isEmpty()) return listOf()
        val nodes = mutableListOf<Node>()
        nodes.add(relationships.first().startNode)
        for (relationship in relationships) {
            nodes.add(relationship.endNode)
        }
        return nodes
    }

    private fun materializeLength(propertyKey: String): Int {
        return relationships
            .map { it.getProperty(propertyKey) }
            .map { cost -> (cost as? Long)?.toString() ?: cost as String }
            .sumOf(String::toLong).toInt()
    }
}