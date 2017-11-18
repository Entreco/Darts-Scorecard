package nl.entreco.domain.play.model

import nl.entreco.domain.play.model.players.NoPlayer
import org.junit.Assert.*
import org.junit.Test

/**
 * Created by Entreco on 18/11/2017.
 */
class WhosNextEmptyTest {

    @Test
    fun `it should return NoPlayer initially`() {
        assertEquals(NoPlayer().toString(), WhosNext().toString())
    }

    @Test
    fun `it should return NoPlayer after starting with no teams`() {
        assertEquals(NoPlayer().toString(), WhosNext().start().toString())
    }
}