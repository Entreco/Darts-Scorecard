package nl.entreco.domain.play.stats

import nl.entreco.domain.BaseUsecase
import nl.entreco.domain.common.executors.Background
import nl.entreco.domain.common.executors.Foreground
import nl.entreco.domain.repository.LiveStatRepository
import javax.inject.Inject

/**
 * Created by entreco on 22/01/2018.
 */
class FetchLiveStatUsecase @Inject constructor(
        private val liveStatRepository: LiveStatRepository,
        bg: Background,
        fg: Foreground) : BaseUsecase(bg, fg) {

    fun exec(req: FetchLiveStatRequest, done: (FetchLiveStatResponse) -> Unit, fail: (Throwable) -> Unit) {
        onBackground({
            val stat = liveStatRepository.fetchStat(req.turnId, req.metaId)
            onUi { done(FetchLiveStatResponse(stat)) }

        }, fail)
    }
}