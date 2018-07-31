package nl.entreco.domain.launch

import nl.entreco.domain.model.players.PlayerSeperator
import nl.entreco.domain.model.players.TeamSeperator

/**
 * Created by entreco on 06/01/2018.
 */
data class ExtractTeamsRequest(private val teamString: String) {
    private val illegalState = IllegalStateException("invalid team string, should be 'player1,player2|player3|player4,player5'")
    private val playerSplit: MutableList<List<String>> = emptyList<List<String>>().toMutableList()
    private lateinit var teamSplit: List<String>

    private val teams: Array<Array<String>> by lazy {

        val teamsArray = ArrayList<Array<String>>()
        teamSplit.forEachIndexed { index, _ ->
            val players = ArrayList<String>()
            playerSplit[index].forEachIndexed { _, player ->
                players.add(player)
            }
            teamsArray.add(players.toTypedArray())
        }

        teamsArray.toTypedArray()

    }

    @Throws(IllegalStateException::class)
    fun validate() {
        val teams = teamString
        if (teams.isEmpty()) throw illegalState
        teamSplit = teams.split(TeamSeperator)
        teamSplit.forEach {
            if (it.isEmpty()) throw illegalState
            val split = it.split(PlayerSeperator)
            split.forEach {
                if (it.trim().isEmpty()) throw illegalState
            }
            playerSplit.add(split)
        }
    }

    fun toPlayerNames(): List<String> {
        val players = ArrayList<String>()
        teams.forEach { players.addAll(it) }
        return players
    }

    override fun toString(): String {
        return teamString
    }
}