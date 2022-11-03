package wtf.hahn.neo4j.util

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ReverseIteratorTest {
    @Test
    fun reverseTest() {
        val sampleList = listOf(1, 2, 3, 4, 5)
        val integers = ReverseIterator(sampleList)
        var listPosition = sampleList.size - 1
        while (integers.hasNext()) {
            Assertions.assertEquals(sampleList[listPosition--], integers.next())
        }
    }
}