package nl.entreco.domain.repository

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Created by Entreco on 18/12/2017.
 */
class RetrieveGameRequestTest {

    @Test
    fun basic() {
        val request = RetrieveGameRequest(22, TeamIdsString("2,3|8"), CreateGameRequest(0, 2, 3, 4))
        assertEquals(22, request.gameId)
        assertEquals("2,3|8", request.teamIds.toString())
        assertEquals(0, request.create.startScore)
        assertEquals(2, request.create.startIndex)
        assertEquals(3, request.create.numLegs)
        assertEquals(4, request.create.numSets)
    }
}