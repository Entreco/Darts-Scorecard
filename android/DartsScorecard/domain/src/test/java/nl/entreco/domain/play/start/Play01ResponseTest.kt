package nl.entreco.domain.play.start

import nl.entreco.domain.model.Game
import nl.entreco.domain.model.players.Team
import nl.entreco.domain.settings.ScoreSettings
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
    @Mock private lateinit var mockSettings: ScoreSettings
    @Mock private lateinit var mockTeam1: Team
    @Mock private lateinit var mockTeam2: Team

    @Test
    fun `it should report equals on same instance`() {
        val response = Play01Response(mockGame, mockSettings, arrayOf(mockTeam1, mockTeam2), "")
        assertEquals(response, response)
    }

    @Test
    fun `it should report equals on new instance`() {
        val response1 = Play01Response(mockGame, mockSettings, arrayOf(mockTeam1, mockTeam2), "")
        val response2 = Play01Response(mockGame, mockSettings, arrayOf(mockTeam1, mockTeam2), "")
        assertEquals(response1, response2)
    }

    @Test
    fun `it should report equal hashcode`() {
        val response = Play01Response(mockGame, mockSettings, arrayOf(mockTeam1, mockTeam2), "")
        assertEquals(response.hashCode(), response.hashCode())
    }

    @Test
    fun `it should report equals hashcode on new instance`() {
        val response1 = Play01Response(mockGame, mockSettings, arrayOf(mockTeam1, mockTeam2), "")
        val response2 = Play01Response(mockGame, mockSettings, arrayOf(mockTeam1, mockTeam2), "")
        assertEquals(response1.hashCode(), response2.hashCode())
    }
}