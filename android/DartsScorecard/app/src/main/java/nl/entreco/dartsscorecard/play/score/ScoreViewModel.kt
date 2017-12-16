package nl.entreco.dartsscorecard.play.score

import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import android.databinding.ObservableInt
import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.domain.Logger
import nl.entreco.domain.play.listeners.PlayerListener
import nl.entreco.domain.play.listeners.ScoreListener
import nl.entreco.domain.play.model.Game
import nl.entreco.domain.play.model.Next
import nl.entreco.domain.play.model.Score
import nl.entreco.domain.play.model.Turn
import nl.entreco.domain.play.model.players.Player
import nl.entreco.domain.play.model.players.Team
import nl.entreco.domain.play.usecase.CreateGameInput
import nl.entreco.domain.settings.ScoreSettings
import javax.inject.Inject

/**
 * Created by Entreco on 18/11/2017.
 */
class ScoreViewModel @Inject constructor(val adapter: ScoreAdapter, private val logger: Logger) : BaseViewModel(), GameLoadable, ScoreListener, PlayerListener {

    val numSets = ObservableInt(0)
    val teams = ObservableArrayList<Team>()
    val scoreSettings = ObservableField<ScoreSettings>(ScoreSettings())
    val uiCallback = ObservableField<UiCallback>()

    override fun startWith(game: Game, settings: CreateGameInput, uiCallback: UiCallback) {
        this.uiCallback.set(uiCallback)
        this.scoreSettings.set(ScoreSettings(settings.startScore, settings.numLegs, settings.numSets, settings.startIndex))
        this.teams.addAll(game.teams())
        this.numSets.set(settings.numSets)
    }

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
        teams.forEachIndexed({ index, _ -> adapter.teamAtIndexTurnUpdate(index, next) })
    }
}