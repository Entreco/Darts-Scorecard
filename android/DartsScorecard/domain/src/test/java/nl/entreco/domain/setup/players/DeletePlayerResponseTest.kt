package nl.entreco.domain.setup.players

import org.junit.Assert
import org.junit.Test

/**
 * Created by entreco on 17/03/2018.
 */
class DeletePlayerResponseTest {
    @Test
    fun getId() {
        val ids = arrayOf(42L).toLongArray()
        Assert.assertEquals(ids, DeletePlayerResponse(ids).ids)
    }

}