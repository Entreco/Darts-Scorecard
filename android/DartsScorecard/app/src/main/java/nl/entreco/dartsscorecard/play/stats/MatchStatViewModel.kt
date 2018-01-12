package nl.entreco.dartsscorecard.play.stats

import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.dartsscorecard.play.score.GameLoadedNotifier
import nl.entreco.dartsscorecard.play.score.UiCallback
import nl.entreco.domain.model.Score
import nl.entreco.domain.model.Turn
import nl.entreco.domain.model.players.Team
import nl.entreco.domain.play.listeners.MatchStatListener
import nl.entreco.domain.play.stats.TeamStat
import javax.inject.Inject

/**
 * Created by entreco on 11/01/2018.
 */
class MatchStatViewModel @Inject constructor() : BaseViewModel(), GameLoadedNotifier<Array<Turn>>, MatchStatListener {
    private val teamStats: MutableList<TeamStat> = emptyList<TeamStat>().toMutableList()
    override fun onLoaded(teams: Array<Team>, scores: Array<Score>, info: Array<Turn>, uiCallback: UiCallback?) {
        teams.forEach {
            teamStats.add(TeamStat(5))
        }
    }
}