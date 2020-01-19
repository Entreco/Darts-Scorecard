package nl.entreco.dartsscorecard.setup.players

import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.domain.launch.ExtractTeamsRequest
import nl.entreco.domain.model.players.PlayerSeperator
import nl.entreco.domain.model.players.TeamSeperator
import javax.inject.Inject

/**
 * Created by Entreco on 29/12/2017.
 */
class PlayersViewModel @Inject constructor(val adapter: PlayerAdapter) : BaseViewModel() {

    fun setupTeams(): ExtractTeamsRequest {
        val teamString = StringBuilder()
        val players = adapter.participants().groupBy { it.teamIndex.get() }.toSortedMap()
        appendTeams(players, teamString)
        return ExtractTeamsRequest(teamString.toString())
    }

    private fun appendTeams(players: Map<Int, List<PlayerViewModel>>, teamString: StringBuilder) {
        players.forEach {
            if (teamString.isNotEmpty()) {
                teamString.append(TeamSeperator)
            }
            appendPlayers(it.value, teamString)
        }
    }

    private fun appendPlayers(team: List<PlayerViewModel>, teamString: StringBuilder) {
        team.forEachIndexed { index, player ->
            if(!player.isHuman.get()) teamString.append("#")
            teamString.append(player.name.get())
            if (index < team.size - 1) {
                teamString.append(PlayerSeperator)
            }
        }
    }
}