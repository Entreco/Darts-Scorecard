package nl.entreco.domain.play.start

import nl.entreco.domain.model.players.Player
import nl.entreco.domain.model.players.Team
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

/**
 * Created by entreco on 14/01/2018.
 */
class RetrieveTeamsResponseTest{
    private val team1 = Team(arrayOf(Player("Guusje"), Player("Eva")))
    private val team2 = Team(arrayOf(Player("Entreco")))
    private val team3 = Team(emptyArray())
    private val response1 = RetrieveTeamsResponse(arrayOf(team1))
    private val response2 = RetrieveTeamsResponse(arrayOf(team1, team2))
    private val response3 = RetrieveTeamsResponse(arrayOf(team1, team2, team3))

    @Test
    fun `should be equal`() {
        assertEquals(response1, response1)
        assertEquals(response1, RetrieveTeamsResponse(arrayOf(team1)))
    }

    @Test
    fun `should not be equal`() {
        assertNotEquals(response1, response2)
        assertNotEquals(response1, response3)
        assertNotEquals(response2, response3)
    }

    @Test
    fun `should have same hashCode`() {
        assertEquals(response1.hashCode(), response1.hashCode())
        assertEquals(response1.hashCode(), RetrieveTeamsResponse(arrayOf(team1)).hashCode())
    }

    @Test
    fun `should have differen hashcode`() {
        assertNotEquals(response1.hashCode(), response2.hashCode())
        assertNotEquals(response1.hashCode(), response3.hashCode())
        assertNotEquals(response2.hashCode(), response3.hashCode())
    }
}