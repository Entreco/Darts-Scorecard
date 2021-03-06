package nl.entreco.domain.settings

import nl.entreco.domain.model.Score

/**
 * Created by Entreco on 13/11/2017.
 */
data class ScoreSettings(val startScore: Int = 501, val numLegs: Int = 3, val numSets: Int = 2, val teamStartIndex: Int = 0) {
    fun score(): Score {
        return Score(startScore, 0, 0, this)
    }
}