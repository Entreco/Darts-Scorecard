package nl.entreco.domain.setup.players

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Created by entreco on 17/03/2018.
 */
class DeletePlayerRequestTest {
    @Test
    fun getId() {
        assertEquals(42, DeletePlayerRequest(42).id)
    }

}