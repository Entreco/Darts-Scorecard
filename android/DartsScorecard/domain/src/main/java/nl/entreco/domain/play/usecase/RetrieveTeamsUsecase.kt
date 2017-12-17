package nl.entreco.domain.play.usecase

import nl.entreco.domain.executors.Background
import nl.entreco.domain.BaseUsecase
import nl.entreco.domain.executors.Foreground
import nl.entreco.domain.play.model.players.Player
import nl.entreco.domain.play.model.players.Team
import nl.entreco.domain.play.model.players.TeamIdsString
import nl.entreco.domain.play.repository.PlayerRepository
import javax.inject.Inject

/**
 * Created by Entreco on 17/12/2017.
 */
class RetrieveTeamsUsecase @Inject constructor(private val playerRepository: PlayerRepository, bg: Background, fg: Foreground) : BaseUsecase(bg, fg){

    fun start(teamIds: TeamIdsString, done: (Array<Team>) -> Unit, fail: (Throwable) -> Unit) {
        onBackground({
            val teams = retrieveTeams(teamIds.toString())
            onUi { done(teams) }
        }, fail)
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