package nl.entreco.dartsscorecard.play

import nl.entreco.domain.play.model.Next
import nl.entreco.domain.play.model.Score
import nl.entreco.domain.play.model.Turn

/**
 * Created by Entreco on 20/11/2017.
 */
interface ScoreListener {
    fun onDartThrown(turn: Turn, next: Next)
    fun onScoreChange(scores: Array<Score>, next: Next)
}