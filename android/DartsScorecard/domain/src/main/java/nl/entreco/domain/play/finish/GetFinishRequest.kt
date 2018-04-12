package nl.entreco.domain.play.finish

import nl.entreco.domain.model.Score
import nl.entreco.domain.model.Turn

/**
 * Created by entreco on 06/01/2018.
 */
class GetFinishRequest(val score: Score, val turn: Turn, private val favDouble: Int) {
    fun score(): Int {
        return score.score
    }

    fun dartsLeft(): Int {
        return turn.dartsLeft()
    }

    fun total(): Int {
        return turn.total()
    }

    fun double(): Int {
        return favDouble
    }
}