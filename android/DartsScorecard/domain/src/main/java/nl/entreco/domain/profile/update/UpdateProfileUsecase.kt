package nl.entreco.domain.profile.update

import nl.entreco.domain.BaseUsecase
import nl.entreco.domain.common.executors.Background
import nl.entreco.domain.common.executors.Foreground
import nl.entreco.domain.repository.ImageRepository
import nl.entreco.domain.repository.ProfileInfoRepository
import javax.inject.Inject

/**
 * Created by entreco on 28/02/2018.
 */
class UpdateProfileUsecase @Inject constructor(
        private val imageRepository: ImageRepository,
        private val profileInfoRepository: ProfileInfoRepository,
        bg: Background, fg: Foreground) : BaseUsecase(bg, fg) {

    fun exec(req: UpdateProfileRequest, done: (UpdateProfileResponse) -> Unit, fail: (Throwable) -> Unit) {
        onBackground({

            // Copy image to local file && resize
            val localImage = imageRepository.copyImageToPrivateAppData(req.image, req.size)
            val profile = profileInfoRepository.update(req.id, req.name, localImage, req.double)
            onUi { done(UpdateProfileResponse(profile)) }
        }, fail)
    }
}
