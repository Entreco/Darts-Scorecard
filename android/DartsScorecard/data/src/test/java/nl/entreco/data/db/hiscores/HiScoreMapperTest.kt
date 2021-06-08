package nl.entreco.data.db.hiscores

import org.junit.Assert.*
import org.junit.Test
import java.util.*
import java.util.Locale

class HiScoreMapperTest {

    @Test
    fun `it should divide and conquer`() {
        val aggregator = 100
        val avg = when (val denominator = 3) {
            0 -> "--"
            else -> "%.2f".format(Locale.UK, aggregator / denominator.toDouble())
        }

        val avgAsString : String = avg
        assertEquals("33.33", avgAsString)
        assertEquals(33.33, avgAsString.toDouble(), 0.01)
    }
}