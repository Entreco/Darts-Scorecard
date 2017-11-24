package nl.entreco.dartsscorecard.play.score

import android.databinding.*
import android.util.Log
import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.dartsscorecard.play.PlayerListener
import nl.entreco.dartsscorecard.play.ScoreListener
import nl.entreco.domain.play.model.Next
import nl.entreco.domain.play.model.Score
import nl.entreco.domain.play.model.Turn
import nl.entreco.domain.play.model.players.Team
import nl.entreco.domain.settings.ScoreSettings
import javax.inject.Inject

/**
 * Created by Entreco on 18/11/2017.
 */
open class ScoreViewModel @Inject constructor(val teams: Array<Team>, val scoreSettings: ScoreSettings, val adapter: TeamScoreAdapter) : BaseViewModel(), ScoreListener, PlayerListener {

    val numSets = ObservableInt(scoreSettings.numSets)

    override fun onScoreChange(scores: Array<Score>, next: Next) {
        Log.d("NoNICE", "1:$scores n:$next")
        scores.forEachIndexed { index, score -> adapter.teamAtIndexScored(index, score, next.player); }
    }

    override fun onDartThrown(turn: Turn, next: Next) {
        Log.d("NoNICE", "onDartThrown: ${turn.last()} from:${next.player}")
        adapter.teamAtIndexThrew(next.teamIndex, turn, next.player)
    }

    override fun onNext(next: Next) {
        Log.d("NoNICE", "onNext: ${next.player}")
    }
}