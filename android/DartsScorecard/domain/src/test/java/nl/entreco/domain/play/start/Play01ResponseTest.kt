package nl.entreco.domain.play.start

import nl.entreco.domain.model.Game
import nl.entreco.domain.model.Turn
import nl.entreco.domain.model.players.Team
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 14/01/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class Play01ResponseTest {

    @Mock private lateinit var mockGame: Game
    @Mock private lateinit var mockTeam1: Team
    @Mock private lateinit var mockTeam2: Team
    @Mock private lateinit var mockTurn1: Turn
    @Mock private lateinit var mockTurn2: Turn

    @Test
    fun `it should report equals on same instance`() {
        val (pair1: Pair<Long, Turn>, pair2: Pair<Long, Turn>) = givenPairs()
        val response = Play01Response(mockGame, arrayOf(mockTeam1, mockTeam2), listOf(pair1, pair2))
        assertEquals(response, response)
    }

    @Test
    fun `it should report equals on new instance`() {
        val (pair1: Pair<Long, Turn>, pair2: Pair<Long, Turn>) = givenPairs()
        val response1 = Play01Response(mockGame, arrayOf(mockTeam1, mockTeam2), listOf(pair1, pair2))
        val response2 = Play01Response(mockGame, arrayOf(mockTeam1, mockTeam2), listOf(pair1, pair2))
        assertEquals(response1, response2)
    }

    @Test
    fun `it should report equal hashcode`() {
        val (pair1: Pair<Long, Turn>, pair2: Pair<Long, Turn>) = givenPairs()
        val response = Play01Response(mockGame, arrayOf(mockTeam1, mockTeam2), listOf(pair1, pair2))
        assertEquals(response.hashCode(), response.hashCode())
    }

    @Test
    fun `it should report equals hashcode on new instance`() {
        val (pair1: Pair<Long, Turn>, pair2: Pair<Long, Turn>) = givenPairs()
        val response1 = Play01Response(mockGame, arrayOf(mockTeam1, mockTeam2), listOf(pair1, pair2))
        val response2 = Play01Response(mockGame, arrayOf(mockTeam1, mockTeam2), listOf(pair1, pair2))
        assertEquals(response1.hashCode(), response2.hashCode())
    }

    private fun givenPairs(): Pair<Pair<Long, Turn>, Pair<Long, Turn>> {
        val pair1: Pair<Long, Turn> = Pair(1, mockTurn1)
        val pair2: Pair<Long, Turn> = Pair(2, mockTurn2)
        return Pair(pair1, pair2)
    }


}