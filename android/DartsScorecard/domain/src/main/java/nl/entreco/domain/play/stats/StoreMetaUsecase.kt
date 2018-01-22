package nl.entreco.domain.play.stats

import nl.entreco.domain.BaseUsecase
import nl.entreco.domain.common.executors.Background
import nl.entreco.domain.common.executors.Foreground
import nl.entreco.domain.play.ScoreEstimator
import nl.entreco.domain.repository.MetaRepository
import javax.inject.Inject

/**
 * Created by entreco on 10/01/2018.
 */
class StoreMetaUsecase @Inject constructor(
        private val metaRepository: MetaRepository,
        private val scoreEstimator: ScoreEstimator,
        bg: Background, fg: Foreground) : BaseUsecase(bg, fg) {

    fun exec(req: StoreMetaRequest, done: (Long, Long) -> Unit, fail: (Throwable) -> Unit) {
        onBackground({

            val atDouble = scoreEstimator.atDouble(req.turn, req.turnMeta.score.score)
            val metaId = metaRepository.create(req.turnId, req.gameId, req.turnMeta, atDouble)
            onUi { done(req.turnId, metaId) }
        }, fail)
    }
}