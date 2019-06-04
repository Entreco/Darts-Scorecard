package nl.entreco.domain.play.start

import nl.entreco.shared.BaseUsecase
import nl.entreco.shared.threading.Background
import nl.entreco.shared.threading.Foreground
import nl.entreco.domain.model.players.DeletedPlayer
import nl.entreco.domain.model.players.Player
import nl.entreco.domain.model.players.Team
import nl.entreco.domain.repository.PlayerRepository
import javax.inject.Inject

/**
 * Created by Entreco on 17/12/2017.
 */
class RetrieveTeamsUsecase @Inject constructor(private val playerRepository: PlayerRepository, bg: Background, fg: Foreground) : BaseUsecase(bg, fg){

    fun exec(request: RetrieveTeamsRequest, done: (RetrieveTeamsResponse) -> Unit, fail: (Throwable) -> Unit) {
        onBackground({
            val teams = retrieveTeams(request.teamIdsString)
            onUi { done(RetrieveTeamsResponse(teams)) }
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
                if(dbPlayer == null){
                    players.add(DeletedPlayer())
                } else {
                    players.add(dbPlayer)
                }
            }

            teams.add(Team(players.toTypedArray()))
        }

        return teams.toTypedArray()
    }
}