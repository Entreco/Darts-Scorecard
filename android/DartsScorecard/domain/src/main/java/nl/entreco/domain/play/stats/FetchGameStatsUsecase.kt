package nl.entreco.domain.play.stats

import nl.entreco.libcore.BaseUsecase
import nl.entreco.libcore.threading.Background
import nl.entreco.libcore.threading.Foreground
import nl.entreco.domain.repository.LiveStatRepository
import javax.inject.Inject

/**
 * Created by entreco on 16/01/2018.
 */
class FetchGameStatsUsecase @Inject constructor(
    private val liveStatRepository: LiveStatRepository,
    bg: nl.entreco.libcore.threading.Background,
    fg: nl.entreco.libcore.threading.Foreground
) : nl.entreco.libcore.BaseUsecase(bg, fg) {

    fun exec(req: FetchGameStatsRequest, done: (FetchGameStatsResponse) -> Unit, fail: (Throwable) -> Unit) {
        onBackground({
            val stats = liveStatRepository.fetchAllForGame(req.gameId)
            onUi { done(FetchGameStatsResponse(req.gameId, stats)) }

        }, fail)
    }
}