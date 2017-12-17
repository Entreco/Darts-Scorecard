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
        assertEquals(1, TeamsString("1,2,3").toTeams().size)
        assertEquals(2, TeamsString("1|2,3").toTeams().size)
    }

    @Test
    fun `should have correct number of total players`() {
        assertEquals(3, TeamsString("1,2,3").toTeams()[0].players.size)
        assertEquals(1, TeamsString("1|2,3").toTeams()[0].players.size)
        assertEquals(2, TeamsString("1|2,3").toTeams()[1].players.size)
    }

    @Test
    fun `should have correct number of players`() {
        assertEquals(3, TeamsString("1,2,3").toPlayers().size)
        assertEquals(3, TeamsString("1|2,3").toPlayers().size)
        assertEquals(3, TeamsString("1|2,3").toPlayers().size)
    }

    @Test
    fun `test one complete team`() {
        val teams = TeamsString("1,2|3|4,5,6").toTeams()
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
        assertEquals("1,2,3", TeamsString("1,2,3").toString())
        assertEquals("1|2,3", TeamsString("1|2,3").toString())
    }

    @Test
    fun `should generate strings from team (1)`() {
        val teams = arrayOf(Team(arrayOf(Player("1"))))
        assertEquals("1", TeamsString.fromTeams(teams))
    }

    @Test
    fun `should generate strings from team (2)`() {
        val teams = arrayOf(Team(arrayOf(Player("1"), Player("2"))), Team(arrayOf(Player("3"))))
        assertEquals("1,2|3", TeamsString.fromTeams(teams))
    }

    @Test
    fun `should generate strings from team (3)`() {
        val teams = arrayOf(Team(arrayOf(Player("1"), Player("2"))), Team(arrayOf(Player("3"))), Team(arrayOf(Player("4"), Player("5"))))
        assertEquals("1,2|3|4,5", TeamsString.fromTeams(teams))
    }
}