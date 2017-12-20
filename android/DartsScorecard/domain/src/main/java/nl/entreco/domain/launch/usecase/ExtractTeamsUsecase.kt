package nl.entreco.domain.launch.usecase

import nl.entreco.domain.BaseUsecase
import nl.entreco.domain.executors.Background
import nl.entreco.domain.executors.Foreground
import nl.entreco.domain.launch.TeamNamesString
import nl.entreco.domain.model.players.Player
import nl.entreco.domain.repository.PlayerRepository
import nl.entreco.domain.repository.TeamIdsString
import javax.inject.Inject

/**
 * Created by Entreco on 16/12/2017.
 */
class ExtractTeamsUsecase @Inject constructor(private val playerRepository: PlayerRepository, bg: Background, fg: Foreground) : BaseUsecase(bg, fg) {

    fun exec(teamNamesInput: TeamNamesString, done: (TeamIdsString) -> Unit, fail: (Throwable) -> Unit) {
        onBackground({
            var teamIds = teamNamesInput.toString()
            teamNamesInput.toPlayerNames().forEach {
                val player = playerRepository.fetchByName(it)
                teamIds = replaceNameWithId(player, it, teamIds)
            }
            onUi { done(TeamIdsString(teamIds)) }
        }, fail)
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