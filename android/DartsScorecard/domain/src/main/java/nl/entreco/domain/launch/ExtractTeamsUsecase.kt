package nl.entreco.domain.launch

import nl.entreco.domain.model.players.Bot
import nl.entreco.domain.model.players.Player
import nl.entreco.domain.repository.BotRepository
import nl.entreco.domain.repository.PlayerRepository
import nl.entreco.shared.BaseUsecase
import nl.entreco.shared.threading.Background
import nl.entreco.shared.threading.Foreground
import javax.inject.Inject

/**
 * Created by Entreco on 16/12/2017.
 */
class ExtractTeamsUsecase @Inject constructor(
        private val playerRepository: PlayerRepository,
        private val botRepository: BotRepository,
        bg: Background, fg: Foreground) : BaseUsecase(bg, fg) {

    fun exec(request: ExtractTeamsRequest, done: (ExtractTeamsResponse) -> Unit, fail: (Throwable) -> Unit) {
        onBackground({
            var teamIds = request.toString()
            request.toPlayerNames().forEach {
                val participant : Player? = when (it.startsWith("#")) {
                    true  -> botRepository.fetchByName(it.substring(1))
                    false -> playerRepository.fetchByName(it)
                }
                teamIds = replaceNameWithId(participant, it, teamIds)
            }

            onUi { done(ExtractTeamsResponse(teamIds)) }

        }, fail)
    }

    private fun replaceNameWithId(participant: Any?, name: String, teamIds: String): String {
        return when (participant) {
            is Bot    -> teamIds.replace(name, "#${participant.id}")
            is Player -> teamIds.replace(name, participant.id.toString())
            else      -> {
                val id = playerRepository.create(name, 0)
                teamIds.replace(name, id.toString())
            }
        }
    }
}