package nl.entreco.domain.play.stats

import nl.entreco.domain.BaseUsecase
import nl.entreco.domain.common.executors.Background
import nl.entreco.domain.common.executors.Foreground
import nl.entreco.domain.repository.StatRepository
import nl.entreco.domain.repository.TurnRepository
import javax.inject.Inject

/**
 * Created by entreco on 16/01/2018.
 */
class FetchGameStatsUsecase @Inject constructor(private val turnRepository: TurnRepository,
                                                private val statRepository: StatRepository, bg: Background, fg: Foreground) : BaseUsecase(bg, fg) {
    fun exec(req: FetchGameStatsRequest, done: (FetchGameStatsResponse) -> Unit, fail: (Throwable) -> Unit) {
        onBackground({
            val stats = statRepository.fetchAllForGame(req.gameId)

            onUi { done(FetchGameStatsResponse(req.gameId)) }

        }, fail)
    }
}