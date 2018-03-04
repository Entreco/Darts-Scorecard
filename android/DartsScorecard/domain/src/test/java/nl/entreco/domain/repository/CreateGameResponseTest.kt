package nl.entreco.domain.repository

import nl.entreco.domain.setup.game.CreateGameResponse
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Created by Entreco on 18/12/2017.
 */
class CreateGameResponseTest {

    @Test
    fun basic() {
        val request = CreateGameResponse(22, "2,3|8", 0, 2, 3, 4)
        assertEquals(22, request.gameId)
        assertEquals("2,3|8", request.teamIds)
        assertEquals(0, request.startScore)
        assertEquals(2, request.startIndex)
        assertEquals(3, request.numLegs)
        assertEquals(4, request.numSets)
    }
}
