package nl.entreco.domain.play.model.players

/**
 * Created by Entreco on 16/12/2017.
 */
data class TeamsString(private val teamString: String) {

    private val teams: Array<Team> by lazy {

        val teamsArray = ArrayList<Team>()
        teamSplit.forEachIndexed { index, _ ->
            val players = ArrayList<Player>()
            playerSplit[index].forEachIndexed { _, player ->
                players.add(Player(player))
            }
            teamsArray.add(Team(players.toTypedArray()))
        }

        teamsArray.toTypedArray()

    }
    private val illegalState = IllegalStateException("invalid team string, should be 'player1,player2|player3|player4,player5'")

    private lateinit var teamSplit: List<String>
    private val playerSplit: MutableList<List<String>> = emptyList<List<String>>().toMutableList()

    init {
        validate(teamString)
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

    fun asTeamArray(): Array<Team> {
        return teams
    }

    fun asPlayersList(): List<Player> {
        val players = ArrayList<Player>()
        teams.forEach { players.addAll(it.players) }
        return players
    }

    fun asString(): String {
        return teamString
    }

    companion object {
        fun parse(teams: Array<Team>) : String {
            val teamString = StringBuilder()
            teams.forEach {

                if (!teamString.isEmpty()){
                    teamString.append("|")
                }

                teamString.append(it.players[0])
                it.players.drop(1).forEach {
                    teamString.append(",")
                    teamString.append(it)
                }
            }

            return teamString.toString()
        }
    }
}