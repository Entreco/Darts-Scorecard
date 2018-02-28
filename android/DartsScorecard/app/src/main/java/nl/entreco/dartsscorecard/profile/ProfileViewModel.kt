package nl.entreco.dartsscorecard.profile

import android.content.Intent
import android.databinding.ObservableField
import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.domain.profile.fetch.FetchProfileRequest
import nl.entreco.domain.profile.fetch.FetchProfileResponse
import nl.entreco.domain.profile.fetch.FetchProfileUsecase
import nl.entreco.domain.profile.update.UpdateProfileRequest
import nl.entreco.domain.profile.update.UpdateProfileResponse
import nl.entreco.domain.profile.update.UpdateProfileUsecase
import javax.inject.Inject

/**
 * Created by entreco on 21/02/2018.
 */
class ProfileViewModel @Inject constructor(private val fetchProfileUsecase: FetchProfileUsecase,
                                           private val updateProfileUsecase: UpdateProfileUsecase) : BaseViewModel() {

    val profile = ObservableField<PlayerProfile>()

    fun fetchProfile(playerIds: LongArray) {
        if (profile.get() == null) {
            fetchProfileUsecase.exec(FetchProfileRequest(playerIds), onProfileSuccess(), onProfileFailed())
        }
    }

    fun showImageForProfile(data: Intent?) {
        val currentProfile = profile.get()
        if (currentProfile != null) {
            // Update Profile Usecase
            currentProfile.image.set(data?.data.toString())
            updateProfileUsecase.exec(UpdateProfileRequest(currentProfile.id, null, null, image = data?.data.toString()), onProfileUpdated(), onProfileFailed())
        }
    }

    private fun onProfileUpdated(): (UpdateProfileResponse) -> Unit = { profile ->
        this.profile.set(PlayerProfile(profile.profile))
    }

    private fun onProfileSuccess(): (FetchProfileResponse) -> Unit = { profile ->
        this.profile.set(PlayerProfile(profile.profiles[0]))
    }

    private fun onProfileFailed(): (Throwable) -> Unit = {}

}
