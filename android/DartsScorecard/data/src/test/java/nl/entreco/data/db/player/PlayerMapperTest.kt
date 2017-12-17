package nl.entreco.data.db.player

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Created by Entreco on 17/12/2017.
 */
class PlayerMapperTest {
    private val subject = PlayerMapper()

    @Test(expected = IllegalStateException::class)
    fun `should throw exception when invalid table`() {
        val table = givenPlayer()
        subject.to(table)
    }

    @Test
    fun `should create Game when valid table`() {
        val table = givenPlayer("henk", 12)
        val actual = subject.to(table)
        assertEquals("henk", actual.name)
        assertEquals(12, actual.prefs.favoriteDouble)
    }

    private fun givenPlayer(name: String = "", fav: Int = 0): PlayerTable {
        val table = PlayerTable()
        table.uid = "some uid"
        table.name = name
        table.fav = fav.toString()
        return table
    }

}