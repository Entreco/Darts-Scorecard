package nl.entreco.domain.profile.fetch

import nl.entreco.domain.BaseUsecase
import nl.entreco.domain.common.executors.Background
import nl.entreco.domain.common.executors.Foreground
import nl.entreco.domain.repository.ProfileRepository
import javax.inject.Inject

/**
 * Created by entreco on 23/02/2018.
 */
class FetchProfileUsecase @Inject constructor(
        private val profileRepository: ProfileRepository,
        bg: Background, fg: Foreground) : BaseUsecase(bg, fg) {

    fun exec(request: FetchProfileRequest, done: (FetchProfileResponse) -> Unit, fail: (Throwable) -> Unit) {
        onBackground({
            val profiles = profileRepository.fetchAll(request.players)
            onUi { done(FetchProfileResponse(profiles)) }
        }, fail)
    }
}
