package nl.entreco.dartsscorecard.play.stats

import android.databinding.ObservableArrayMap
import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.dartsscorecard.play.score.GameLoadedNotifier
import nl.entreco.dartsscorecard.play.score.UiCallback
import nl.entreco.domain.model.Score
import nl.entreco.domain.model.players.Team
import nl.entreco.domain.play.start.Play01Response
import nl.entreco.domain.play.stats.FetchGameStatsRequest
import nl.entreco.domain.play.stats.FetchGameStatsUsecase
import javax.inject.Inject

/**
 * Created by entreco on 11/01/2018.
 */
class MatchStatViewModel @Inject constructor(private val fetchGameStatsUsecase: FetchGameStatsUsecase) : BaseViewModel(), GameLoadedNotifier<Play01Response> {

    val teamStats = ObservableArrayMap<Int, TeamStatModel>()
    private lateinit var teams: Array<Team>

    override fun onLoaded(teams: Array<Team>, scores: Array<Score>, info: Play01Response, uiCallback: UiCallback?) {
        initializeStats(teams)
        fetchGameStatsUsecase.exec(FetchGameStatsRequest(info.game.id, info.teamIds),
                { response ->
                    teams.forEachIndexed { index, team ->
                        val stats = team.players.mapNotNull {
                            response.stats[it.id]
                        }

                        teamStats[index]?.append(stats)
                    }
                },
                {})
    }

    private fun initializeStats(teams: Array<Team>) {
        this.teams = teams
        teams.forEachIndexed { index, team ->
            teamStats[index] = TeamStatModel(team)
        }
    }
}