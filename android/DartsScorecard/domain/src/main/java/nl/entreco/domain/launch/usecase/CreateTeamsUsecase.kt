package nl.entreco.domain.launch.usecase

import nl.entreco.domain.BaseUsecase
import nl.entreco.domain.executors.Background
import nl.entreco.domain.executors.Foreground
import nl.entreco.domain.launch.TeamNamesString
import nl.entreco.domain.model.players.Player
import nl.entreco.domain.model.players.Team
import nl.entreco.domain.repository.PlayerRepository
import nl.entreco.domain.repository.TeamIdsString
import javax.inject.Inject

/**
 * Created by Entreco on 16/12/2017.
 */
class CreateTeamsUsecase @Inject constructor(private val playerRepository: PlayerRepository, private val bg: Background, private val fg: Foreground) : BaseUsecase(bg, fg) {

    fun retrieve(teamIds: TeamIdsString, done: (Array<Team>) -> Unit, fail: (Throwable) -> Unit) {
        onBackground({
            val teams = ArrayList<Team>().toMutableList()
            val teamSplit = teamIds.toString().split("|")
            teamSplit.forEach {

                val players = ArrayList<Player>()
                val playerSplit = it.split(",")
                playerSplit.forEach {
                    val player = playerRepository.fetchById(it.toLong())!!
                    players.add(player)
                }

                teams.add(Team(players.toTypedArray()))
            }

            onUi {
                done(teams.toTypedArray())
            }
        }, fail)
    }

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