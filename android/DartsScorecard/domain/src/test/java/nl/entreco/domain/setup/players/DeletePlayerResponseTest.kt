package nl.entreco.domain.setup.players

import org.junit.Assert
import org.junit.Test

/**
 * Created by entreco on 17/03/2018.
 */
class DeletePlayerResponseTest {
    @Test
    fun getId() {
        Assert.assertEquals(42, DeletePlayerResponse(42).id)
    }

}