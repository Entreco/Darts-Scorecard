package nl.entreco.domain.model.players

import org.junit.Assert
import org.junit.Test

/**
 * Created by entreco on 20/03/2018.
 */
class DeletedPlayerTest{
    @Test
    fun `it should have 'no player' as name for NoPlayer()`() {
        Assert.assertEquals("Deleted Player", DeletedPlayer().toString())
    }
}