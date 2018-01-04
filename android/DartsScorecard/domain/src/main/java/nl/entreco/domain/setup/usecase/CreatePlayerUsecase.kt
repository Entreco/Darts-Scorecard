package nl.entreco.domain.setup.usecase

import nl.entreco.domain.BaseUsecase
import nl.entreco.domain.executors.Background
import nl.entreco.domain.executors.Foreground
import nl.entreco.domain.model.players.Player
import nl.entreco.domain.model.players.PlayerPrefs
import nl.entreco.domain.repository.PlayerRepository
import javax.inject.Inject

/**
 * Created by Entreco on 02/01/2018.
 */
class CreatePlayerUsecase @Inject constructor(private var playerRepository: PlayerRepository, bg: Background, fg: Foreground) : BaseUsecase(bg, fg) {
    fun exec(req: CreatePlayerRequest, done: (Player) -> Unit, fail: (Throwable) -> Unit) {
        onBackground({
            val lowercaseName = req.name.toLowerCase()
            val playerId = playerRepository.create(lowercaseName, req.double)
            onUi { done(Player(lowercaseName, playerId, PlayerPrefs(req.double))) }
        }, fail)
    }
}