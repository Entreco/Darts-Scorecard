package nl.entreco.domain.setup.players

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Created by entreco on 17/03/2018.
 */
class DeletePlayerRequestTest {
    @Test
    fun getId() {
        val ids = arrayOf(1L,2L).toLongArray()
        assertEquals(ids, DeletePlayerRequest(ids).ids)
    }

}