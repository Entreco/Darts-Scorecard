package nl.entreco.domain.splash.usecase

import nl.entreco.domain.executors.Background
import nl.entreco.domain.executors.Foreground
import nl.entreco.domain.model.players.Player
import nl.entreco.domain.model.players.TeamIdsString
import nl.entreco.domain.splash.TeamNamesString
import nl.entreco.domain.repository.PlayerRepository
import javax.inject.Inject

/**
 * Created by Entreco on 16/12/2017.
 */
class CreateTeamsUsecase @Inject constructor(private val playerRepository: PlayerRepository, private val bg: Background, private val fg: Foreground) {

    fun start(teamNamesInput: TeamNamesString, done: (TeamIdsString) -> Unit, fail: (Throwable) -> Unit) {
        bg.post(Runnable {

            try {

                var teamIds = teamNamesInput.toString()
                teamNamesInput.toPlayerNames().forEach {
                    val player = playerRepository.fetchByName(it)
                    teamIds = replaceNameWithId(player, it, teamIds)
                }

                fg.post(Runnable {
                    done(TeamIdsString(teamIds))
                })
            } catch (oops: Exception) {
                fg.post(Runnable { fail(oops) })
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