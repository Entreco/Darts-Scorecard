package nl.entreco.domain.play.description

import nl.entreco.shared.BaseUsecase
import nl.entreco.shared.threading.Background
import nl.entreco.shared.threading.Foreground
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