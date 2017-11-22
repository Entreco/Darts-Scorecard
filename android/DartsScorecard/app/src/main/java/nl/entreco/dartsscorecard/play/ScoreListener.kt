package nl.entreco.dartsscorecard.play

import nl.entreco.domain.play.model.Next
import nl.entreco.domain.play.model.Score

/**
 * Created by Entreco on 20/11/2017.
 */
interface ScoreListener {
    fun onDartThrown(dart: Int, next: Next)
    fun onScoreChange(scores: Array<Score>, next: Next)
}