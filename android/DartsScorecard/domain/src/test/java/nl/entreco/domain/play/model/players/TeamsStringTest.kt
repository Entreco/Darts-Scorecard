package nl.entreco.domain.play.model.players

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

/**
 * Created by Entreco on 16/12/2017.
 */
class TeamsStringTest {

    @Test(expected = IllegalStateException::class)
    fun `invalid team strings, empty`() {
        TeamsString("")
    }
    @Test(expected = IllegalStateException::class)
    fun `invalid team strings, '|'`() {
        TeamsString("|")
    }
    @Test(expected = IllegalStateException::class)
    fun `invalid team strings, '1,2|'`() {
        TeamsString("1,2|")
    }
    @Test(expected = IllegalStateException::class)
    fun `invalid team strings, '|1'`() {
        TeamsString("|1")
    }
    @Test(expected = IllegalStateException::class)
    fun `invalid team strings, ',|'`() {
        TeamsString(",|")
    }
    @Test(expected = IllegalStateException::class)
    fun `invalid team strings, '|,'`() {
        TeamsString("|,")
    }
    @Test(expected = IllegalStateException::class)
    fun `invalid team strings, '||'`() {
        TeamsString("||")
    }
    @Test(expected = IllegalStateException::class)
    fun `invalid team strings, ',,'`() {
        TeamsString(",,")
    }

    @Test
    fun `valid team strings`() {
        assertNotNull(TeamsString("player1"))
        assertNotNull(TeamsString("player1,player2"))
    }

    @Test
    fun `should have correct number of teams`() {
        assertEquals(1, TeamsString("1,2,3").asTeamArray().size)
        assertEquals(2, TeamsString("1|2,3").asTeamArray().size)
    }

    @Test
    fun `should have correct number of total players`() {
        assertEquals(3, TeamsString("1,2,3").asTeamArray()[0].players.size)
        assertEquals(1, TeamsString("1|2,3").asTeamArray()[0].players.size)
        assertEquals(2, TeamsString("1|2,3").asTeamArray()[1].players.size)
    }

    @Test
    fun `should have correct number of players`() {
        assertEquals(3, TeamsString("1,2,3").asPlayersList().size)
        assertEquals(3, TeamsString("1|2,3").asPlayersList().size)
        assertEquals(3, TeamsString("1|2,3").asPlayersList().size)
    }

    @Test
    fun `test one complete team`() {
        val teams = TeamsString("1,2|3|4,5,6").asTeamArray()
        assertEquals(3, teams.size)
        assertEquals("1", teams[0].players[0].name)
        assertEquals("2", teams[0].players[1].name)
        assertEquals("3", teams[1].players[0].name)
        assertEquals("4", teams[2].players[0].name)
        assertEquals("5", teams[2].players[1].name)
        assertEquals("6", teams[2].players[2].name)
    }

    @Test
    fun `should print correct string`() {
        assertEquals("1,2,3", TeamsString("1,2,3").asString())
        assertEquals("1|2,3", TeamsString("1|2,3").asString())
    }
}