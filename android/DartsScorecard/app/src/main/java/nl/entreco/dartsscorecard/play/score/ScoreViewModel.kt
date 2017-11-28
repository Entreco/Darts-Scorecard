package nl.entreco.dartsscorecard.play.score

import android.databinding.*
import android.util.Log
import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.dartsscorecard.play.PlayerListener
import nl.entreco.dartsscorecard.play.ScoreListener
import nl.entreco.domain.Logger
import nl.entreco.domain.play.model.Next
import nl.entreco.domain.play.model.Score
import nl.entreco.domain.play.model.Turn
import nl.entreco.domain.play.model.players.Player
import nl.entreco.domain.play.model.players.Team
import nl.entreco.domain.settings.ScoreSettings
import javax.inject.Inject

/**
 * Created by Entreco on 18/11/2017.
 */
open class ScoreViewModel @Inject constructor(val teams: Array<Team>, val scoreSettings: ScoreSettings, val adapter: ScoreAdapter, val logger: Logger) : BaseViewModel(), ScoreListener, PlayerListener {

    val numSets = ObservableInt(scoreSettings.numSets)

    override fun onScoreChange(scores: Array<Score>, by: Player) {
        logger.d("NoNICE", "1:$scores by:$by")
        scores.forEachIndexed { index, score -> adapter.teamAtIndexScored(index, score, by); }
    }

    override fun onDartThrown(turn: Turn, by: Player) {
        logger.d("NoNICE", "onDartThrown: ${turn.last()} from:$by")
        val index = teams.indexOfFirst { it.contains(by) }
        adapter.teamAtIndexThrew(index, turn, by)
    }

    override fun onNext(next: Next) {
        logger.d("NoNICE", "onNext: ${next.player}")
        teams.forEachIndexed({ index ,_ -> adapter.teamAtIndexTurnUpdate(index, next)})
    }
}