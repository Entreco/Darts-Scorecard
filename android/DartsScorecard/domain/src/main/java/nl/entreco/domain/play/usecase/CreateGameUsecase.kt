package nl.entreco.domain.play.usecase

import nl.entreco.domain.play.model.Arbiter
import nl.entreco.domain.play.model.Game
import nl.entreco.domain.play.repository.GameRepository
import javax.inject.Inject

/**
 * Created by Entreco on 14/11/2017.
 */
open class CreateGameUsecase @Inject constructor(private val arbiter: Arbiter, private val gameRepository: GameRepository) {
    open fun start() : Game {
        return gameRepository.new(arbiter).apply { start() }
    }
}