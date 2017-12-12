package nl.entreco.domain.play.usecase

import nl.entreco.domain.executors.Background
import nl.entreco.domain.executors.Foreground
import nl.entreco.domain.play.model.Game
import nl.entreco.domain.play.repository.GameRepository
import javax.inject.Inject

/**
 * Created by Entreco on 14/11/2017.
 */
open class RetrieveGameUsecase @Inject constructor(private val gameRepository: GameRepository, private val bg: Background,
                                                   private val fg: Foreground) {

    open fun start(uid: String, settings: SetupModel, ok: (Game) -> Unit, err: (Throwable) -> Unit) {
        bg.post(Runnable {

            try {
                val game = gameRepository.fetchLatest()
                fg.post(Runnable {
                    ok(game)
                })
            } catch (ohno: Exception) {
                fg.post(Runnable { err(ohno) })
            }
        })
    }
}