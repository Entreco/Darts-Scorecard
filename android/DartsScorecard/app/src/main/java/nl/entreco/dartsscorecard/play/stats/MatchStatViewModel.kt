package nl.entreco.dartsscorecard.play.stats

import android.databinding.ObservableArrayList
import android.databinding.ObservableArrayMap
import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.widget.AdapterView
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

    val team0 = ObservableField<TeamStatModel>()
    val team1 = ObservableField<TeamStatModel>()
    val teamEntries = ObservableArrayList<String>()
    val teamStats = ObservableArrayMap<Int, TeamStatModel>()

    val team0Index = ObservableInt()
    val team1Index = ObservableInt()

    private lateinit var teams: Array<Team>

    fun onTeamStat0Selected(adapter: AdapterView<*>, index: Int){
        val resolved = adapter.getItemAtPosition(index).toString()
        val selected = teams.indexOfFirst { it.toString() == resolved }
        team0.set(teamStats[selected])
        team0Index.set(selected)
    }

    fun onTeamStat1Selected(adapter: AdapterView<*>, index: Int){
        val resolved = adapter.getItemAtPosition(index).toString()
        val selected = teams.indexOfFirst { it.toString() == resolved }
        team1.set(teamStats[selected])
        team1Index.set(selected)
    }

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

        teamEntries.addAll(teams.map { it.toString().capitalize() })
        team0Index.set(0)
        team1Index.set(if(teams.size > 1) 1 else 0)
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