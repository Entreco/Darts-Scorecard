package nl.entreco.domain.launch

import org.junit.Assert
import org.junit.Test

/**
 * Created by entreco on 06/01/2018.
 */
class ExtractTeamsRequestTest {
    @Test(expected = IllegalStateException::class)
    fun `invalid team strings, empty`() {
        ExtractTeamsRequest("")
    }

    @Test(expected = IllegalStateException::class)
    fun `invalid team strings, '|'`() {
        ExtractTeamsRequest("|")
    }

    @Test(expected = IllegalStateException::class)
    fun `invalid team strings, '1,2|'`() {
        ExtractTeamsRequest("1,2|")
    }

    @Test(expected = IllegalStateException::class)
    fun `invalid team strings, '|1'`() {
        ExtractTeamsRequest("|1")
    }

    @Test(expected = IllegalStateException::class)
    fun `invalid team strings, ',|'`() {
        ExtractTeamsRequest(",|")
    }

    @Test(expected = IllegalStateException::class)
    fun `invalid team strings, '|,'`() {
        ExtractTeamsRequest("|,")
    }

    @Test(expected = IllegalStateException::class)
    fun `invalid team strings, '||'`() {
        ExtractTeamsRequest("||")
    }

    @Test(expected = IllegalStateException::class)
    fun `invalid team strings, ',,'`() {
        ExtractTeamsRequest(",,")
    }

    @Test
    fun `valid team strings`() {
        Assert.assertNotNull(ExtractTeamsRequest("player1"))
        Assert.assertNotNull(ExtractTeamsRequest("player1,player2"))
    }

    @Test
    fun `should have correct number of players`() {
        Assert.assertEquals(3, ExtractTeamsRequest("1,2,3").toPlayerNames().size)
        Assert.assertEquals(3, ExtractTeamsRequest("1|2,3").toPlayerNames().size)
        Assert.assertEquals(3, ExtractTeamsRequest("1|2,3").toPlayerNames().size)
    }

    @Test
    fun `should print correct string`() {
        Assert.assertEquals("1,2,3", ExtractTeamsRequest("1,2,3").toString())
        Assert.assertEquals("1|2,3", ExtractTeamsRequest("1|2,3").toString())
    }
}