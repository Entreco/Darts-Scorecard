package nl.entreco.domain.play.start

import nl.entreco.domain.BaseUsecase
import nl.entreco.domain.common.executors.Background
import nl.entreco.domain.common.executors.Foreground
import nl.entreco.domain.repository.GameRepository
import javax.inject.Inject

/**
 * Created by Entreco on 14/11/2017.
 */
class RetrieveGameUsecase @Inject constructor(private val gameRepository: GameRepository,
                                              bg: Background,
                                              fg: Foreground) : BaseUsecase(bg, fg) {

    fun start(request: RetrieveGameRequest, ok: (RetrieveGameResponse) -> Unit, err: (Throwable) -> Unit) {
        onBackground({
            val game = gameRepository.fetchBy(request.gameId)
            onUi { ok(RetrieveGameResponse(game)) }
        }, err)
    }
}