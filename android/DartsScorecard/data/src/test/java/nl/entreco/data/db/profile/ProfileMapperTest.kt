package nl.entreco.data.db.profile

import nl.entreco.data.db.player.PlayerTable
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

/**
 * Created by entreco on 26/02/2018.
 */
class ProfileMapperTest {

    private lateinit var subject: ProfileMapper
    private lateinit var table: PlayerTable

    @Before
    fun setUp() {
        subject = ProfileMapper()
    }

    @Test(expected = IllegalStateException::class)
    fun `it should throw if name is invalid`() {
        givenTable(null)
        subject.to(table)
    }

    @Test(expected = IllegalStateException::class)
    fun `it should throw if name is empty`() {
        givenTable(null)
        subject.to(table)
    }

    @Test
    fun `it should NOT throw if name is valid`() {
        givenTable("Some Player name")
        assertNotNull(subject.to(table))
    }

    @Test(expected = IllegalStateException::class)
    fun `it should throw if trying to update incorrect player (mismatch in ids)`() {
        subject.from(12, PlayerTable(),"name")
    }
    
    @Test
    fun `it should set name if non-null`() {
        val table = PlayerTable()
        table.id = 12

        val actual = subject.from(12, table,"name")
        assertEquals(12, actual.id)
        assertEquals("name", actual.name)
    }

    private fun givenTable(name: String?) {
        table = PlayerTable()
        table.id = 12
        table.image = "img"
        table.fav = "12"
        if (name != null) {
            table.name = name
        }
    }

}
