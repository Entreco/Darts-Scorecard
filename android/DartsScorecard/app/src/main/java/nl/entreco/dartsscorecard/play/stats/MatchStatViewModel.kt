package nl.entreco.dartsscorecard.play.stats

import android.databinding.ObservableArrayMap
import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.dartsscorecard.play.score.GameLoadedNotifier
import nl.entreco.dartsscorecard.play.score.UiCallback
import nl.entreco.domain.model.Score
import nl.entreco.domain.model.players.Team
import nl.entreco.domain.setup.game.CreateGameRequest
import javax.inject.Inject

/**
 * Created by entreco on 11/01/2018.
 */
class MatchStatViewModel @Inject constructor() : BaseViewModel(), GameLoadedNotifier<CreateGameRequest> {

    private lateinit var teams: Array<Team>
    val teamStats = ObservableArrayMap<Int, TeamStatModel>()

    override fun onLoaded(teams: Array<Team>, scores: Array<Score>, info: CreateGameRequest, uiCallback: UiCallback?) {
        this.teams = teams
        teams.forEachIndexed { index, team ->
            teamStats.put(index, TeamStatModel(team.toString()))
        }
    }
}