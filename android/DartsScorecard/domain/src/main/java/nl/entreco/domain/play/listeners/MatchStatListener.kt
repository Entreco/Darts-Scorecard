package nl.entreco.domain.play.listeners

import nl.entreco.domain.model.Next
import nl.entreco.domain.model.Turn
import nl.entreco.domain.model.players.Player

/**
 * Created by entreco on 11/01/2018.
 */
interface MatchStatListener {
    fun onStatsChange(next: Next, turn: Turn, by: Player)
}