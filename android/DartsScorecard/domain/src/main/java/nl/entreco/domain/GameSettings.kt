package nl.entreco.domain

/**
 * Created by Entreco on 13/11/2017.
 */
data class GameSettings (val startScore: Int = 501, val numPlayers: Int = 2){
    val scoreSettings : ScoreSettings = ScoreSettings(startScore)
    val initial : Score = Score(startScore, 0, 0, scoreSettings)
}