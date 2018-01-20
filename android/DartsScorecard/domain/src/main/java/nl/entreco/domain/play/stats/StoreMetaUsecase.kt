package nl.entreco.domain.play.stats

import nl.entreco.domain.BaseUsecase
import nl.entreco.domain.common.executors.Background
import nl.entreco.domain.common.executors.Foreground
import nl.entreco.domain.repository.MetaRepository
import javax.inject.Inject

/**
 * Created by entreco on 10/01/2018.
 */
class StoreMetaUsecase @Inject constructor(
        private val metaRepository: MetaRepository,
        bg: Background, fg: Foreground) : BaseUsecase(bg, fg) {

    fun exec(req: StoreMetaRequest, fail: (Throwable) -> Unit) {
        onBackground({

            val turn = req.turn
            val startScore = req.turnMeta.score
            val dartsAtCheckout = 2
            // TODO: this should be finished, by estimating darts at checkout
            metaRepository.create(req.turnId, req.gameId, req.turnMeta)
        }, fail)
    }
}