package nl.entreco.domain.launch

import nl.entreco.shared.BaseUsecase
import nl.entreco.shared.threading.Background
import nl.entreco.shared.threading.Foreground
import nl.entreco.domain.model.players.Player
import nl.entreco.domain.repository.PlayerRepository
import javax.inject.Inject

/**
 * Created by Entreco on 16/12/2017.
 */
class ExtractTeamsUsecase @Inject constructor(private val playerRepository: PlayerRepository, bg: Background, fg: Foreground) : BaseUsecase(bg, fg) {

    fun exec(request: ExtractTeamsRequest, done: (ExtractTeamsResponse) -> Unit, fail: (Throwable) -> Unit) {
        onBackground({
            var teamIds = request.toString()
            request.toPlayerNames().forEach {
                val player = playerRepository.fetchByName(it)
                teamIds = replaceNameWithId(player, it, teamIds)
            }
            onUi { done(ExtractTeamsResponse(teamIds)) }
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