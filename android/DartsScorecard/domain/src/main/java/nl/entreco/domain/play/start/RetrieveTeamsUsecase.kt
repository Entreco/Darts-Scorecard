package nl.entreco.domain.play.start

import nl.entreco.libcore.BaseUsecase
import nl.entreco.libcore.threading.Background
import nl.entreco.libcore.threading.Foreground
import nl.entreco.domain.model.players.DeletedPlayer
import nl.entreco.domain.model.players.Player
import nl.entreco.domain.model.players.Team
import nl.entreco.domain.repository.BotRepository
import nl.entreco.domain.repository.PlayerRepository
import javax.inject.Inject

/**
 * Created by Entreco on 17/12/2017.
 */
class RetrieveTeamsUsecase @Inject constructor(
    private val playerRepository: PlayerRepository,
    private val botRepository: BotRepository,
    bg: nl.entreco.libcore.threading.Background, fg: nl.entreco.libcore.threading.Foreground
) : nl.entreco.libcore.BaseUsecase(bg, fg){

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
            playerSplit.forEach { s ->
                val dbPlayer = when{
                    s.startsWith("#") -> botRepository.fetchById(s.substring(1).toLong())
                    else -> playerRepository.fetchById(s.toLong())
                }
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