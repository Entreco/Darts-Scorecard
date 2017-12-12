package nl.entreco.domain.play.usecase

import nl.entreco.domain.executors.Background
import nl.entreco.domain.executors.Foreground
import nl.entreco.domain.play.model.Game
import nl.entreco.domain.play.repository.GameRepository
import javax.inject.Inject

/**
 * Created by Entreco on 12/12/2017.
 */
class CreateGameUsecase @Inject constructor(private val gameRepository: GameRepository, private val bg: Background, private val fg: Foreground) {

    interface Callback {
        fun onGameCreated(game: Game, setup: SetupModel)
        fun onGameFailed(err: Throwable)
    }

    fun start(setupModel: SetupModel, callback: Callback) {
        bg.post(Runnable {
            val game = gameRepository.create(setupModel)
            fg.post(Runnable {
                callback.onGameCreated(game, setupModel)
            })
        })
    }
}