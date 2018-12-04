package nl.entreco.dartsscorecard.play.score

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import nl.entreco.dartsscorecard.ad.AdViewModel
import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.domain.model.Next
import nl.entreco.domain.model.Score
import nl.entreco.domain.model.Turn
import nl.entreco.domain.model.players.Player
import nl.entreco.domain.model.players.Team
import nl.entreco.domain.play.description.FetchMatchDescriptionUsecase
import nl.entreco.domain.play.listeners.PlayerListener
import nl.entreco.domain.play.listeners.ScoreListener
import nl.entreco.domain.settings.ScoreSettings
import javax.inject.Inject

/**
 * Created by Entreco on 18/11/2017.
 */
class ScoreViewModel @Inject constructor(
        val adapter: ScoreAdapter,
        private val fetchMatchDescriptionUsecase: FetchMatchDescriptionUsecase) : BaseViewModel(), GameLoadedNotifier<ScoreSettings>, ScoreListener, PlayerListener {

    val description = ObservableField<String>("")
    val firstTo = ObservableInt(0)
    val numSets = ObservableInt(0)
    val showSets = ObservableBoolean(true)
    val teams = ObservableArrayList<Team>()
    val currentTeam = ObservableInt()
    val scores = ObservableArrayList<Score>()
    val scoreSettings = ObservableField<ScoreSettings>(ScoreSettings())
    val uiCallback = ObservableField<UiCallback>()

    override fun onLoaded(teams: Array<Team>, scores: Array<Score>, info: ScoreSettings, uiCallback: UiCallback?) {
        this.uiCallback.set(uiCallback)
        this.scoreSettings.set(info)
        this.scores.clear()
        this.scores.addAll(scores)
        this.teams.clear()
        this.teams.addAll(teams)
        this.numSets.set(info.numSets)
        this.showSets.set(info.numSets > 1)
        this.firstTo.set(if(info.numSets > 1) info.numSets else info.numLegs)
        this.fetchMatchDescriptionUsecase.go({ response -> description.set(response.description.description) }, { /* TODO: Set default value */})
    }

    override fun onScoreChange(scores: Array<Score>, by: Player) {
        scores.forEachIndexed { index, score -> adapter.teamAtIndexScored(index, score, by) }
    }

    override fun onDartThrown(turn: Turn, by: Player) {
        val index = teams.indexOfFirst { it.contains(by) }
        adapter.teamAtIndexThrew(index, turn, by)
    }

    override fun onNext(next: Next) {
        currentTeam.set(teams.indexOf(next.team))
        teams.forEachIndexed { index, _ -> adapter.teamAtIndexTurnUpdate(index, next) }
    }
}