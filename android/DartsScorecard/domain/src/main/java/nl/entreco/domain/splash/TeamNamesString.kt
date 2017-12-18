package nl.entreco.domain.splash

/**
 * Created by Entreco on 16/12/2017.
 */
data class TeamNamesString(private val teamString: String) {

    private val illegalState = IllegalStateException("invalid team string, should be 'player1,player2|player3|player4,player5'")
    private val playerSplit: MutableList<List<String>> = emptyList<List<String>>().toMutableList()
    private lateinit var teamSplit: List<String>

    init {
        validate(teamString)
    }

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

    private fun validate(teams: String) {
        if (teams.isEmpty()) throw illegalState
        teamSplit = teams.split("|")
        teamSplit.forEach {
            if (it.isEmpty()) throw illegalState
            val split = it.split(",")
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