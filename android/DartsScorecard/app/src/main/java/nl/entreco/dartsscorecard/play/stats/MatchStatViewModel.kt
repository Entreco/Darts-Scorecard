package nl.entreco.dartsscorecard.play.stats

import android.databinding.ObservableArrayMap
import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.dartsscorecard.play.score.GameLoadedNotifier
import nl.entreco.dartsscorecard.play.score.UiCallback
import nl.entreco.domain.model.Next
import nl.entreco.domain.model.Score
import nl.entreco.domain.model.Turn
import nl.entreco.domain.model.players.Player
import nl.entreco.domain.model.players.Team
import nl.entreco.domain.play.listeners.MatchStatListener
import javax.inject.Inject

/**
 * Created by entreco on 11/01/2018.
 */
class MatchStatViewModel @Inject constructor() : BaseViewModel(), GameLoadedNotifier<List<Pair<Long, Turn>>>, MatchStatListener {

    private lateinit var teams: Array<Team>
    val teamStats = ObservableArrayMap<Int, TeamStatModel>()

    override fun onLoaded(teams: Array<Team>, scores: Array<Score>, info: List<Pair<Long, Turn>>, uiCallback: UiCallback?) {
        this.teams = teams
        teams.forEachIndexed { index, team ->
            val turns = info.filter { team.contains(it.first) }.map { it.second }
            teamStats.put(index, TeamStatModel(team.toString(), turns.toTypedArray()))
        }
    }

    override fun onStatsChange(next: Next, turn: Turn, by: Player, scores: Array<Score>) {
        teams.forEachIndexed { index, team ->
            if (team.contains(by)) {
                teamStats.put(index, teamStats[index]?.applyTurn(turn))
            }
        }
    }
}