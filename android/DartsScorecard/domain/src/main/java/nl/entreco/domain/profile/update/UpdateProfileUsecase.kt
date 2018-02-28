package nl.entreco.domain.profile.update

import nl.entreco.domain.BaseUsecase
import nl.entreco.domain.common.executors.Background
import nl.entreco.domain.common.executors.Foreground
import nl.entreco.domain.repository.ProfileRepository
import javax.inject.Inject

/**
 * Created by entreco on 28/02/2018.
 */
class UpdateProfileUsecase @Inject constructor(private val profileRepository: ProfileRepository,
                                               bg: Background, fg: Foreground) : BaseUsecase(bg, fg) {

    fun exec(req: UpdateProfileRequest, done: (UpdateProfileResponse) -> Unit, fail: (Throwable) -> Unit) {
        onBackground({
            val profile = profileRepository.update(req.id, req.name, req.image, req.double)
            onUi { done(UpdateProfileResponse(profile)) }
        }, fail)
    }
}
