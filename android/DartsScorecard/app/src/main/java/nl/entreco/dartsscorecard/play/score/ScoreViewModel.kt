package nl.entreco.dartsscorecard.play.score

import android.databinding.ObservableArrayList
import android.databinding.ObservableArrayMap
import android.databinding.ObservableInt
import android.databinding.ObservableList
import android.util.Log
import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.dartsscorecard.play.PlayerListener
import nl.entreco.dartsscorecard.play.ScoreListener
import nl.entreco.domain.play.model.Next
import nl.entreco.domain.play.model.Score
import nl.entreco.domain.play.model.players.Team
import nl.entreco.domain.settings.ScoreSettings
import javax.inject.Inject

/**
 * Created by Entreco on 18/11/2017.
 */
class ScoreViewModel @Inject constructor(val teams: Array<Team>, val scoreSettings: ScoreSettings) : BaseViewModel(), ScoreListener {

    val scores = ObservableArrayList<Score>()
    val numSets = ObservableInt(scoreSettings.numSets)

    init {
        for (team in teams) {
            scores.add(scoreSettings.score())
        }
    }

    override fun onScoreChange(scores: Array<Score>, next: Next) {
        Log.d("NoNICE", "1:$scores n:$next")
        for(index in 0 until scores.size){
            this.scores[index] = scores[index]
        }

    }
}