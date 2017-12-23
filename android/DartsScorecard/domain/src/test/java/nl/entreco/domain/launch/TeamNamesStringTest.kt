package nl.entreco.domain.launch

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

/**
 * Created by Entreco on 16/12/2017.
 */
class TeamNamesStringTest {

    @Test(expected = IllegalStateException::class)
    fun `invalid team strings, empty`() {
        TeamNamesString("")
    }
    @Test(expected = IllegalStateException::class)
    fun `invalid team strings, '|'`() {
        TeamNamesString("|")
    }
    @Test(expected = IllegalStateException::class)
    fun `invalid team strings, '1,2|'`() {
        TeamNamesString("1,2|")
    }
    @Test(expected = IllegalStateException::class)
    fun `invalid team strings, '|1'`() {
        TeamNamesString("|1")
    }
    @Test(expected = IllegalStateException::class)
    fun `invalid team strings, ',|'`() {
        TeamNamesString(",|")
    }
    @Test(expected = IllegalStateException::class)
    fun `invalid team strings, '|,'`() {
        TeamNamesString("|,")
    }
    @Test(expected = IllegalStateException::class)
    fun `invalid team strings, '||'`() {
        TeamNamesString("||")
    }
    @Test(expected = IllegalStateException::class)
    fun `invalid team strings, ',,'`() {
        TeamNamesString(",,")
    }

    @Test
    fun `valid team strings`() {
        assertNotNull(TeamNamesString("player1"))
        assertNotNull(TeamNamesString("player1,player2"))
    }

    @Test
    fun `should have correct number of players`() {
        assertEquals(3, TeamNamesString("1,2,3").toPlayerNames().size)
        assertEquals(3, TeamNamesString("1|2,3").toPlayerNames().size)
        assertEquals(3, TeamNamesString("1|2,3").toPlayerNames().size)
    }

    @Test
    fun `should print correct string`() {
        assertEquals("1,2,3", TeamNamesString("1,2,3").toString())
        assertEquals("1|2,3", TeamNamesString("1|2,3").toString())
    }
}