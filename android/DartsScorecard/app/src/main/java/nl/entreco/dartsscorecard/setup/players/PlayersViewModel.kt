package nl.entreco.dartsscorecard.setup.players

import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.domain.Logger
import nl.entreco.domain.launch.TeamNamesString
import nl.entreco.domain.model.players.PlayerSeperator
import nl.entreco.domain.model.players.TeamSeperator
import javax.inject.Inject

/**
 * Created by Entreco on 29/12/2017.
 */
class PlayersViewModel @Inject constructor(val adapter: PlayerAdapter, private val logger: Logger) : BaseViewModel() {

    fun setupTeams(): TeamNamesString {
        val teamString = StringBuilder()
        val players = adapter.playersMap().groupBy { it.teamIndex.get() }.toSortedMap()
        appendTeams(players, teamString)
        return TeamNamesString(teamString.toString())
    }

    private fun appendTeams(players: Map<Int, List<PlayerViewModel>>, teamString: StringBuilder) {
        players.forEach({
            if (!teamString.isEmpty()) {
                teamString.append(TeamSeperator)
            }
            appendPlayers(it.value, teamString)
        })
    }

    private fun appendPlayers(team: List<PlayerViewModel>, teamString: StringBuilder) {
        team.forEachIndexed { index, player ->
            teamString.append(player.name.get())
            if (index < team.size - 1) {
                teamString.append(PlayerSeperator)
            }
        }
    }

    fun handlePlayerUpdated(oldName: String, playerName: String, teamIndex: Int) {
        adapter.replacePlayer(oldName, playerName, teamIndex)
    }
}