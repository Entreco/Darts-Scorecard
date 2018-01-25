package nl.entreco.domain.play.stats

import nl.entreco.domain.BaseUsecase
import nl.entreco.domain.common.executors.Background
import nl.entreco.domain.common.executors.Foreground
import nl.entreco.domain.repository.StatRepository
import javax.inject.Inject

/**
 * Created by entreco on 22/01/2018.
 */
class FetchGameStatUsecase @Inject constructor(
        private val statRepository: StatRepository,
        bg: Background,
        fg: Foreground) : BaseUsecase(bg, fg) {

    fun exec(req: FetchGameStatRequest, done: (FetchGameStatResponse) -> Unit, fail: (Throwable) -> Unit) {
        onBackground({
            val stat = statRepository.fetchStat(req.turnId, req.metaId)
            onUi { done(FetchGameStatResponse(stat)) }

        }, fail)
    }
}