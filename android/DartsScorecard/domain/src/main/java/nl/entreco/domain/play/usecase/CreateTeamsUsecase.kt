package nl.entreco.domain.play.usecase

import nl.entreco.domain.executors.Background
import nl.entreco.domain.executors.Foreground
import nl.entreco.domain.play.model.players.Player
import nl.entreco.domain.play.model.players.TeamIdsString
import nl.entreco.domain.play.model.players.TeamNamesString
import nl.entreco.domain.play.repository.PlayerRepository
import javax.inject.Inject

/**
 * Created by Entreco on 16/12/2017.
 */
class CreateTeamsUsecase @Inject constructor(private val playerRepository: PlayerRepository, private val bg: Background, private val fg: Foreground) {
    interface Callback {
        fun onTeamsCreated(teamIds: TeamIdsString)
        fun onTeamsFailed(err: Throwable)
    }

    fun start(teamNamesInput: TeamNamesString, callback: CreateTeamsUsecase.Callback) {
        bg.post(Runnable {

            try {

                var teamIds = teamNamesInput.toString()
                teamNamesInput.toPlayerNames().forEach {
                    val player = playerRepository.fetchByName(it)
                    teamIds = replaceNameWithId(player, it, teamIds)
                }

                fg.post(Runnable {
                    callback.onTeamsCreated(TeamIdsString(teamIds))
                })
            } catch (oops: Exception) {
                fg.post(Runnable { callback.onTeamsFailed(oops) })
            }
        })
    }

    private fun replaceNameWithId(player: Player?, name: String, teamIds: String): String {
        return if (player == null) {
            val id = playerRepository.create(name, 0) // TODO: Get Favorite Double
            teamIds.replace(name, id.toString())
        } else {
            teamIds.replace(name, player.id.toString())
        }
    }
}