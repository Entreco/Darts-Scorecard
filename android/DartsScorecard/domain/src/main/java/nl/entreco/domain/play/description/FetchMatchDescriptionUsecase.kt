package nl.entreco.domain.play.description

import nl.entreco.domain.BaseUsecase
import nl.entreco.domain.common.executors.Background
import nl.entreco.domain.common.executors.Foreground
import nl.entreco.domain.repository.MatchDescriptionRepository
import javax.inject.Inject

class FetchMatchDescriptionUsecase @Inject constructor(private val repo: MatchDescriptionRepository,
                                                       bg: Background,
                                                       fg: Foreground) : BaseUsecase(bg, fg) {

    fun go(done: (FetchMatchDescriptionResponse) -> Unit, fail: (Throwable) -> Unit) {
        onBackground({
            val description = repo.fetchIt()
            onUi { done(FetchMatchDescriptionResponse(description)) }
        }, fail)
    }
}