package nl.entreco.dartsscorecard.play.score

import nl.entreco.domain.play.model.Score

/**
 * Created by Entreco on 19/11/2017.
 */
interface ScoreKeeper {
    fun onScoreChanged(scores: Array<Score>)
}