package nl.entreco.data.db.profile

import nl.entreco.data.db.player.PlayerTable
import org.junit.Assert.assertNotNull
import org.junit.Test

/**
 * Created by entreco on 26/02/2018.
 */
class ProfileMapperTest {

    private lateinit var table : PlayerTable

    @Test(expected = IllegalStateException::class)
    fun `it should throw if name is invalid`() {
        givenTable(null)
        ProfileMapper().to(table)
    }

    @Test(expected = IllegalStateException::class)
    fun `it should throw if name is empty`() {
        givenTable(null)
        ProfileMapper().to(table)
    }

    @Test
    fun `it should NOT throw if name is valid`() {
        givenTable("Some Player name")
        assertNotNull(ProfileMapper().to(table))
    }

    private fun givenTable(name: String?) {
        table = PlayerTable()
        table.id = 12
        table.image = "img"
        table.fav = "12"
        if(name != null){
            table.name = name
        }
    }

}
