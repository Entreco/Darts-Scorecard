package nl.entreco.domain.play.model

import nl.entreco.domain.play.model.players.NoPlayer
import org.junit.Assert.*
import org.junit.Test

/**
 * Created by Entreco on 18/11/2017.
 */
class TurnHandlerEmptyTest {

    @Test
    fun `it should return NoPlayer initially`() {
        assertEquals(NoPlayer().toString(), TurnHandler().toString())
    }

    @Test
    fun `it should return NoPlayer after starting with no teams`() {
        assertEquals(NoPlayer().toString(), TurnHandler().start().toString())
    }
}