package nl.entreco.domain.play.model.players

import org.junit.Assert.assertNotNull
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
        assertNotNull(Team())
    }
}