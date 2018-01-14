package nl.entreco.domain.play.start

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Created by entreco on 14/01/2018.
 */
class Play01RequestTest {

    private val gameId : Long = 2222
    private val teamIds : String = "1|2"
    private val startScore: Int = 1001
    private val startIndex: Int = -1
    private val legs: Int = 100000
    private val sets: Int = -100000
    private val subject = Play01Request(gameId, teamIds, startScore, startIndex, legs, sets)

    @Test
    fun `it should create request with correct parameters`() {
        assertEquals(startScore, subject.createRequest().startScore)
        assertEquals(startIndex, subject.createRequest().startIndex)
        assertEquals(legs, subject.createRequest().numLegs)
        assertEquals(sets, subject.createRequest().numSets)
    }

}