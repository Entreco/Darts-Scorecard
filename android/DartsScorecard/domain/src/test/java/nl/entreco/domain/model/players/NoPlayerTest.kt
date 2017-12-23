package nl.entreco.domain.model.players

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Created by Entreco on 16/12/2017.
 */
class NoPlayerTest {

    @Test
    fun `it should have 'no player' as name for NoPlayer()`() {
        assertEquals("no player", NoPlayer().toString())
    }
}