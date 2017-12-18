package nl.entreco.domain.play.usecase

import nl.entreco.domain.executors.Background
import nl.entreco.domain.BaseUsecase
import nl.entreco.domain.executors.Foreground
import nl.entreco.domain.model.Game
import nl.entreco.domain.repository.GameRepository
import javax.inject.Inject

/**
 * Created by Entreco on 14/11/2017.
 */
class RetrieveGameUsecase @Inject constructor(private val gameRepository: GameRepository,
                                              bg: Background,
                                              fg: Foreground) : BaseUsecase(bg, fg) {

    fun start(gameId: Long, ok: (Game) -> Unit, err: (Throwable) -> Unit) {
        onBackground({
            val game = gameRepository.fetchBy(gameId)
            onUi({ ok(game) })
        }, err)
    }
}