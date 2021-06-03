package nl.entreco.domain.play.description

import nl.entreco.libcore.BaseUsecase
import nl.entreco.libcore.threading.Background
import nl.entreco.libcore.threading.Foreground
import nl.entreco.domain.repository.MatchDescriptionRepository
import javax.inject.Inject

class FetchMatchDescriptionUsecase @Inject constructor(private val repo: MatchDescriptionRepository,
                                                       bg: nl.entreco.libcore.threading.Background,
                                                       fg: nl.entreco.libcore.threading.Foreground
) : nl.entreco.libcore.BaseUsecase(bg, fg) {

    fun go(done: (FetchMatchDescriptionResponse) -> Unit, fail: (Throwable) -> Unit) {
        onBackground({
            val description = repo.fetchIt()
            onUi { done(FetchMatchDescriptionResponse(description)) }
        }, fail)
    }
}