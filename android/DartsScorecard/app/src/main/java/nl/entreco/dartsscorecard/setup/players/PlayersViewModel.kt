package nl.entreco.dartsscorecard.setup.players

import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.domain.launch.TeamNamesString
import javax.inject.Inject

/**
 * Created by Entreco on 29/12/2017.
 */
class PlayersViewModel @Inject constructor(val adapter: PlayerAdapter) : BaseViewModel() {

    fun setupTeams(): TeamNamesString {
        val teamString = StringBuilder()
        val players = adapter.playersMap()
        players.groupBy { it.teamIndex.get() }.forEach({
            if (!teamString.isEmpty()) {
                teamString.append("|")
            }
            appendTeam(it.value, teamString)
        })

        return TeamNamesString(teamString.toString())
    }

    private fun appendTeam(team: List<PlayerViewModel>, teamString: StringBuilder) {
        team.forEachIndexed { index, player ->
            teamString.append(player.name.get())
            if (index < team.size - 1) {
                teamString.append(",")
            }
        }
    }

    fun addPlayer() {
        adapter.onAddPlayer()
    }
}