package nl.entreco.domain.model

import nl.entreco.domain.model.players.Player
import nl.entreco.domain.model.players.Team
import org.junit.Assert.*
import org.junit.Test

/**
 * Created by Entreco on 18/12/2017.
 */
class NextTest {

    @Test
    fun basic() {
        val player = Player("1")
        val next = Next(State.LEG, Team(arrayOf(player)), 108, player, Score())

        assertEquals(State.LEG, next.state)
        assertEquals(1, next.team.players.size)
        assertEquals(108, next.teamIndex)
        assertEquals(player, next.player)
        assertEquals(Score(), next.requiredScore)
    }
}