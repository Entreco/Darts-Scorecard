package nl.entreco.domain.play.listeners

import nl.entreco.domain.model.Next
import nl.entreco.domain.model.Score
import nl.entreco.domain.model.State
import nl.entreco.domain.model.Turn
import nl.entreco.domain.model.players.Player
import nl.entreco.domain.play.listeners.events.*

/**
 * Created by Entreco on 05/12/2017.
 */
interface SpecialEventListener<in T : SpecialEvent> {

    fun handle(event: T)

    fun onSpecialEvent(next: Next, turn: Turn, by: Player, scores: Array<Score>) {
        handleNoScore(turn)
        handleBust(next)
        handleThrown(turn)
        handleNineDarter(turn, scores, next, by)
    }

    private fun handleThrown(turn: Turn) {
        try {
            val event = ThrownEvent(turn.asFinish()) as T
            handle(event)
        } catch (ignore: ClassCastException) {
        }
    }

    private fun handleBust(next: Next) {
        if (next.state == State.ERR_BUST) {
            try {
                val event = BustEvent() as T
                handle(event)
            } catch (ignore: ClassCastException) {
            }
        }
    }

    private fun handleNoScore(turn: Turn) {
        try {
            val event = NoScoreEvent(turn.total() == 0) as T
            handle(event)
        } catch (ignore: ClassCastException) {
        }
    }

    private fun handleNineDarter(turn: Turn, scores: Array<Score>, next: Next, by: Player) {
        try {
            val event = NineDartEvent(turn.dartsLeft() == 0 && isPossibleNineDarter(turn.total(), scores[next.teamIndex].score), by) as T
            handle(event)
        } catch (ignore: ClassCastException) {
        }
    }

    private fun isPossibleNineDarter(scored: Int, score: Int): Boolean {
        return when {
            scored == 180 && score == 321 -> true
            scored == 177 && score == 324 -> true
            scored == 171 && score == 330 -> true
            scored == 167 && score == 334 -> true
            scored == 180 && score == 150 -> true
            scored == 180 && score == 141 -> true
            scored == 180 && score == 144 -> true
            scored == 177 && score == 147 -> true
            scored == 177 && score == 144 -> true
            scored == 171 && score == 150 -> true
            scored == 167 && score == 167 -> true
            else -> false
        }
    }
}