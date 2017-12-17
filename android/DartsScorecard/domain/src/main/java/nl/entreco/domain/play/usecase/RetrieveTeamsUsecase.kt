package nl.entreco.domain.play.usecase

import nl.entreco.domain.executors.Background
import nl.entreco.domain.executors.Foreground
import nl.entreco.domain.play.model.players.Player
import nl.entreco.domain.play.model.players.Team
import nl.entreco.domain.play.model.players.TeamIdsString
import nl.entreco.domain.play.repository.PlayerRepository
import javax.inject.Inject

/**
 * Created by Entreco on 17/12/2017.
 */
class RetrieveTeamsUsecase @Inject constructor(private val playerRepository: PlayerRepository, private val bg: Background, private val fg: Foreground) {

    fun start(teamIds: TeamIdsString, callable: (Array<Team>) -> Unit) {
        bg.post(Runnable {
            val teams = retrieveTeams(teamIds.toString())
            fg.post(Runnable { callable(teams) })
        })
    }

    private fun retrieveTeams(teamIdsString: String): Array<Team> {
        val teams = ArrayList<Team>()
        val teamSplit = teamIdsString.split("|")
        teamSplit.forEach {
            val players = ArrayList<Player>()

            val playerSplit = it.split(",")
            playerSplit.forEach {
                val dbPlayer = playerRepository.fetchById(it.toLong())
                players.add(dbPlayer!!)
            }

            teams.add(Team(players.toTypedArray()))
        }

        return teams.toTypedArray()
    }
}