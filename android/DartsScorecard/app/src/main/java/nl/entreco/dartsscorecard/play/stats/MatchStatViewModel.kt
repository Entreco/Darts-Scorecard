package nl.entreco.dartsscorecard.play.stats

import android.databinding.ObservableArrayMap
import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.dartsscorecard.play.score.GameLoadedNotifier
import nl.entreco.dartsscorecard.play.score.UiCallback
import nl.entreco.domain.Logger
import nl.entreco.domain.model.Score
import nl.entreco.domain.model.players.Team
import nl.entreco.domain.play.listeners.StatListener
import nl.entreco.domain.play.start.Play01Response
import nl.entreco.domain.play.stats.*
import javax.inject.Inject

/**
 * Created by entreco on 11/01/2018.
 */
class MatchStatViewModel @Inject constructor(private val fetchGameStatsUsecase: FetchGameStatsUsecase, private val fetchGameStatUsecase: FetchGameStatUsecase, private val logger: Logger) : BaseViewModel(), GameLoadedNotifier<Play01Response>, StatListener {

    val teamStats = ObservableArrayMap<Int, TeamStatModel>()
    private lateinit var teams: Array<Team>

    override fun onLoaded(teams: Array<Team>, scores: Array<Score>, info: Play01Response, uiCallback: UiCallback?) {
        initializeStats(teams)
        fetchGameStatsUsecase.exec(FetchGameStatsRequest(info.game.id, info.teamIds),
                onStatsFetched(teams),
                onStatsFailed())
    }

    private fun initializeStats(teams: Array<Team>) {
        this.teamStats.clear()
        this.teams = teams
        teams.forEachIndexed { index, team ->
            teamStats[index] = TeamStatModel(team)
        }
    }

    private fun onStatsFetched(teams: Array<Team>): (FetchGameStatsResponse) -> Unit {
        return { response ->
            teams.forEachIndexed { index, team ->
                val stats = team.players.mapNotNull {
                    response.stats[it.id]
                }
                teamStats[index]?.append(stats)
            }
        }
    }

    private fun onStatsFailed(): (Throwable) -> Unit = { err -> logger.e(err.localizedMessage) }

    override fun onStatsChange(turnId: Long, metaId: Long) {
        fetchGameStatUsecase.exec(FetchGameStatRequest(turnId, metaId), onStatUpdated(), onStatsFailed())
    }

    private fun onStatUpdated(): (FetchGameStatResponse) -> Unit {
        return { response ->
            val teamIndex = teams.indexOfFirst { it.contains(response.stat.playerId) }
            if (teamIndex >= 0) {
                teamStats[teamIndex]?.append(listOf(response.stat))
            }
        }
    }
}