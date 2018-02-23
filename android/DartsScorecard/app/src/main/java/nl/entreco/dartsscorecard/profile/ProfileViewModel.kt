package nl.entreco.dartsscorecard.profile

import android.databinding.ObservableField
import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.domain.profile.fetch.FetchProfileRequest
import nl.entreco.domain.profile.fetch.FetchProfileResponse
import nl.entreco.domain.profile.fetch.FetchProfileUsecase
import javax.inject.Inject

/**
 * Created by entreco on 21/02/2018.
 */
class ProfileViewModel @Inject constructor(private val fetchProfileUsecase: FetchProfileUsecase) : BaseViewModel() {

    val profile = ObservableField<PlayerProfile>()

    fun fetchProfile(playerIds: LongArray) {
        if (profile.get() == null) {
            fetchProfileUsecase.exec(FetchProfileRequest(playerIds), onProfileSuccess(), onProfileFailed())
        }
    }

    private fun onProfileSuccess(): (FetchProfileResponse) -> Unit = { profile ->
        this.profile.set(PlayerProfile(profile.profiles[0]))
    }

    private fun onProfileFailed(): (Throwable) -> Unit = {}

}
