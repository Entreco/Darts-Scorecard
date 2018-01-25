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
@Suppress("UNCHECKED_CAST")
interface SpecialEventListener<in T : SpecialEvent> {

    fun handle(event: T)

    fun onSpecialEvent(next: Next, turn: Turn, by: Player, scores: Array<Score>) {
        handleNoScore(turn)
        handleBust(next)
        handleThrown(turn)

        if (playing501(scores)) {
            handleNineDarter(turn, scores, next, by)
        }
    }

    fun playing501(scores: Array<Score>) =
            scores[0].settings.startScore == 501

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
            val canItBePossible = canItBePossible(turn, scores, next)
            val event = NineDartEvent(canItBePossible, by) as T
            handle(event)
        } catch (ignore: ClassCastException) {
        }
    }

    private fun canItBePossible(turn: Turn, scores: Array<Score>, next: Next) : Boolean{
        val score = previousTeamIndex(next.teamIndex, scores).score
        val pnd = isPossibleNineDarter(turn.total(), score)
        return turn.dartsLeft() == 0 && pnd
    }

    private fun previousTeamIndex(index: Int, scores: Array<Score>): Score {
        var previousIndex = index - 1
        if (previousIndex < 0) {
            previousIndex = scores.size - 1
        }
        return scores[previousIndex]
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