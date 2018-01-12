package nl.entreco.domain.play.stats

import nl.entreco.domain.BaseUsecase
import nl.entreco.domain.common.executors.Background
import nl.entreco.domain.common.executors.Foreground
import nl.entreco.domain.repository.StatRepository
import javax.inject.Inject

/**
 * Created by entreco on 10/01/2018.
 */
class StoreStatUsecase @Inject constructor(
        private val statRepository: StatRepository,
        bg: Background, fg: Foreground) : BaseUsecase(bg, fg) {

    fun exec(req: StoreStatRequest, fail: (Throwable) -> Unit) {
        onBackground({
            statRepository.create(req.playerId, req.turnId, req.gameId, req.stats)
        }, fail)
    }
}