package nl.entreco.domain.setup.players

import nl.entreco.domain.BaseUsecase
import nl.entreco.domain.common.executors.Background
import nl.entreco.domain.common.executors.Foreground
import nl.entreco.domain.model.players.Player
import nl.entreco.domain.model.players.PlayerPrefs
import nl.entreco.domain.repository.PlayerRepository
import javax.inject.Inject

/**
 * Created by Entreco on 02/01/2018.
 */
class CreatePlayerUsecase @Inject constructor(private var playerRepository: PlayerRepository, bg: Background, fg: Foreground) : BaseUsecase(bg, fg) {
    fun exec(req: CreatePlayerRequest, done: (CreatePlayerResponse) -> Unit, fail: (Throwable) -> Unit) {
        onBackground({
            val validLowercaseName = getValidLowerCaseName(req.name)
            val playerId = playerRepository.create(validLowercaseName, req.double)
            onUi { done(CreatePlayerResponse(Player(validLowercaseName, playerId, PlayerPrefs(req.double)))) }
        }, fail)
    }

    private fun getValidLowerCaseName(name: String): String {
        val lowerCase = name.toLowerCase()
        if(lowerCase.isBlank()) throw InvalidPlayerNameException("$name is invalid")
        return lowerCase
    }
}