package nl.entreco.domain.play.listeners

import nl.entreco.domain.play.listeners.events.BustEvent
import nl.entreco.domain.play.listeners.events.NoScoreEvent
import nl.entreco.domain.play.listeners.events.OneEightyEvent
import nl.entreco.domain.play.listeners.events.SpecialEvent
import nl.entreco.domain.play.model.Next
import nl.entreco.domain.play.model.Score
import nl.entreco.domain.play.model.Turn
import nl.entreco.domain.play.model.players.Player
import nl.entreco.domain.play.model.players.State

/**
 * Created by Entreco on 05/12/2017.
 */
interface SpecialEventListener<in T : SpecialEvent> {

    fun handle(event: T)

    fun onSpecialEvent(next: Next, turn: Turn, by: Player, scores: Array<Score>) {
        handleOneEight(turn)
        handleNoScore(turn)
        handleBust(next)
    }

    fun handleBust(next: Next) {
        if (next.state == State.ERR_BUST) {
            try {
                val event = BustEvent() as T
                handle(event)
            } catch (ignore: ClassCastException) {
            }
        }
    }

    fun handleNoScore(turn: Turn) {
        try {
            val event = NoScoreEvent(turn.total() == 0) as T
            handle(event)
        } catch (ignore: ClassCastException) {
        }
    }

    private fun handleOneEight(turn: Turn) {
        if (turn.total() == 180) {
            try {
                val event = OneEightyEvent() as T
                handle(event)
            } catch (ignore: ClassCastException) {
            }
        }
    }
}