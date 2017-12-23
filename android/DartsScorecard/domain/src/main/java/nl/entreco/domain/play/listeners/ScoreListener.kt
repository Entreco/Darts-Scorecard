package nl.entreco.domain.play.listeners

import nl.entreco.domain.model.Score
import nl.entreco.domain.model.Turn
import nl.entreco.domain.model.players.Player

/**
 * Created by Entreco on 20/11/2017.
 */
interface ScoreListener {
    fun onDartThrown(turn: Turn, by: Player)
    fun onScoreChange(scores: Array<Score>, by: Player)
}