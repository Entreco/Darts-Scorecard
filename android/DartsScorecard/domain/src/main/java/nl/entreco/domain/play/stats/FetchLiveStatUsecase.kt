package nl.entreco.domain.play.stats

import nl.entreco.domain.repository.LiveStatRepository
import nl.entreco.libcore.BaseUsecase
import nl.entreco.libcore.threading.Background
import nl.entreco.libcore.threading.Foreground
import javax.inject.Inject

/**
 * Created by entreco on 22/01/2018.
 */
class FetchLiveStatUsecase @Inject constructor(
    private val liveStatRepository: LiveStatRepository,
    bg: Background,
    fg: Foreground,
) : BaseUsecase(bg, fg) {

    fun exec(req: FetchLiveStatRequest, done: (FetchLiveStatResponse) -> Unit, fail: (Throwable) -> Unit) {
        onBackground({
            val stat = liveStatRepository.fetchStat(req.turnId, req.metaId)
            onUi { done(FetchLiveStatResponse(stat)) }

        }, fail)
    }
}