package nl.entreco.domain.play.finish

import nl.entreco.domain.model.Score
import nl.entreco.domain.model.Turn

/**
 * Created by entreco on 06/01/2018.
 */
data class GetFinishRequest(val score: Score, val turn: Turn, val favDouble: Int) {
    fun score(): Int {
        return score.score
    }

    fun turn(): Turn {
        return turn.copy()
    }

    fun double(): Int {
        return favDouble
    }
}