package nl.entreco.domain.play.start

import nl.entreco.domain.BaseUsecase
import nl.entreco.domain.common.executors.Background
import nl.entreco.domain.common.executors.Foreground
import nl.entreco.domain.repository.GameRepository
import javax.inject.Inject

/**
 * Created by entreco on 09/01/2018.
 */
class MarkGameAsFinishedUsecase @Inject constructor(private val gameRepository: GameRepository, bg: Background, fg: Foreground) : BaseUsecase(bg, fg) {
    fun exec(request: MarkGameAsFinishedRequest) {
        onBackground({
            gameRepository.finish(request.gameId, request.winningTeam)
        }, {})
    }
}