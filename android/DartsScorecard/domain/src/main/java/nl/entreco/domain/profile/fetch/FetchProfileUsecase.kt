package nl.entreco.domain.profile.fetch

import nl.entreco.domain.repository.ProfileInfoRepository
import nl.entreco.libcore.BaseUsecase
import nl.entreco.libcore.threading.Background
import nl.entreco.libcore.threading.Foreground
import javax.inject.Inject

/**
 * Created by entreco on 23/02/2018.
 */
class FetchProfileUsecase @Inject constructor(
    private val profileInfoRepository: ProfileInfoRepository,
    bg: Background, fg: Foreground,
) : BaseUsecase(bg, fg) {

    fun exec(request: FetchProfileRequest, done: (FetchProfileResponse) -> Unit, fail: (Throwable) -> Unit) {
        onBackground({
            val profiles = profileInfoRepository.fetchAll(request.players)
            onUi { done(FetchProfileResponse(profiles)) }
        }, fail)
    }
}
