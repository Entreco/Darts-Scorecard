package nl.entreco.domain.hiscores

import nl.entreco.domain.BaseUsecase
import nl.entreco.domain.repository.HiScoreRepository
import nl.entreco.shared.threading.Background
import nl.entreco.shared.threading.Foreground
import javax.inject.Inject

class FetchHiScoresUsecase @Inject constructor(
        bg: Background, fg: Foreground,
        private val service: HiScoreRepository
) :
        BaseUsecase(bg, fg) {

    fun go(done: (FetchHiScoreResponse) -> Unit,
           fail: (Throwable) -> Unit) {
        onBackground({
            val hiscores = service.fetchHiscores()
            onUi { done(FetchHiScoreResponse(hiscores)) }
        }, fail)
    }
}