package nl.entreco.dartsscorecard.play.score

import android.databinding.ObservableField
import android.util.Log
import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.domain.play.model.Score
import nl.entreco.domain.settings.ScoreSettings
import javax.inject.Inject

/**
 * Created by Entreco on 18/11/2017.
 */
class ScoreViewModel @Inject constructor(scoreSettings: ScoreSettings) : BaseViewModel(), ScoreKeeper {

    val numSets = ObservableField<String>(scoreSettings.numSets.toString())
    val score1 = ObservableField<String>(scoreSettings.startScore.toString())
    val score2 = ObservableField<String>(scoreSettings.startScore.toString())
    val leg1 = ObservableField<String>(0.toString())
    val leg2 = ObservableField<String>(0.toString())
    val set1 = ObservableField<String>(0.toString())
    val set2 = ObservableField<String>(0.toString())

    override fun onScoreChanged(scores: Array<Score>) {
        Log.d("NoNICE", "1:${scores[0]} 2:${scores[1]}")
        score1.set(scores[0].score.toString())
        score2.set(scores[1].score.toString())
        leg1.set(scores[0].leg.toString())
        leg2.set(scores[1].leg.toString())
        set1.set(scores[0].set.toString())
        set2.set(scores[1].set.toString())
    }
}