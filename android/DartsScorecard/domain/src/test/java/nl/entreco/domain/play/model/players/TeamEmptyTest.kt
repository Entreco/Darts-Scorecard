package nl.entreco.domain.play.model.players

import nl.entreco.domain.play.model.players.NoPlayer
import nl.entreco.domain.play.model.players.Team
import org.junit.Assert.*
import org.junit.Test

/**
 * Created by Entreco on 18/11/2017.
 */
class TeamEmptyTest {
    @Test
    fun `it should have nonnull list of players`() {
        assertNotNull(Team().players)
    }

    @Test
    fun `it should have non null currentPlayer initially`() {
        assertNotNull(Team().currentPlayer)
    }

    @Test
    fun `it should have NoPlayer initially`() {
        assertTrue(Team().currentPlayer is NoPlayer)
    }
}