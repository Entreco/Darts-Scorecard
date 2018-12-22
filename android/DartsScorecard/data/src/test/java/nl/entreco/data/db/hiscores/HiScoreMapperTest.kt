package nl.entreco.data.db.hiscores

import org.junit.Assert.*
import org.junit.Test

class HiScoreMapperTest {

    @Test
    fun `it should divide and conquer`() {
        val aggregator = 100
        val denominator = 3

        val avg = when (denominator) {
            0 -> "--"
            else -> "%.2f".format(aggregator / denominator.toDouble())
        }

        val avgAsString : String = avg
        assertEquals("33.33", avgAsString)
        assertEquals(33.33, avgAsString.toDouble(), 0.01)
    }
}