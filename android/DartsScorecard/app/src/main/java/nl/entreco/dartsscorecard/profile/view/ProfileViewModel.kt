package nl.entreco.dartsscorecard.profile.view

import android.content.Intent
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.domain.model.players.PlayerPrefs
import nl.entreco.domain.profile.Profile
import nl.entreco.domain.profile.ProfileStat
import nl.entreco.domain.profile.fetch.FetchProfileRequest
import nl.entreco.domain.profile.fetch.FetchProfileResponse
import nl.entreco.domain.profile.fetch.FetchProfileStatRequest
import nl.entreco.domain.profile.fetch.FetchProfileStatResponse
import nl.entreco.domain.profile.fetch.FetchProfileStatsUsecase
import nl.entreco.domain.profile.fetch.FetchProfileUsecase
import nl.entreco.domain.profile.update.UpdateProfileRequest
import nl.entreco.domain.profile.update.UpdateProfileResponse
import nl.entreco.domain.profile.update.UpdateProfileUsecase
import javax.inject.Inject

/**
 * Created by entreco on 21/02/2018.
 */
class ProfileViewModel @Inject constructor(
    private val fetchProfileUsecase: FetchProfileUsecase,
    private val updateProfileUsecase: UpdateProfileUsecase,
    private val fetchProfileStatsUsecase: FetchProfileStatsUsecase,
) : BaseViewModel() {

    val numberOfProfiles = ObservableInt()
    val profile = ObservableField<PlayerProfile>()
    val stats = ObservableField<PlayerStats>()
    val errorMsg = ObservableInt()

    fun fetchProfile(playerIds: LongArray) {
        if (profile.get() == null && playerIds.isNotEmpty()) {
            fetchProfileUsecase.exec(FetchProfileRequest(playerIds), onProfileSuccess(), onProfileFailed())
            fetchProfileStatsUsecase.exec(FetchProfileStatRequest(playerIds), onStatsSuccess(), onStatsFailed())
        }
    }

    fun showImageForProfile(data: Intent?, size: Float) {
        val currentProfile = profile.get()
        if (currentProfile != null) {
            // Update Profile Usecase
            currentProfile.image.set(data?.data.toString())
            updateProfileUsecase.exec(UpdateProfileRequest(currentProfile.id, null, null, image = data?.data.toString(), size = size), onProfileUpdated(), onProfileFailed())
        }
    }

    fun showNameForProfile(name: String, double: Int) {
        val currentProfile = profile.get()
        if (currentProfile != null) {
            // Update Profile Usecase
            currentProfile.name.set(name)
            currentProfile.fav.set(double)
            updateProfileUsecase.exec(UpdateProfileRequest(currentProfile.id, name, "$double", null, 0F), onProfileUpdated(), onProfileFailed())
        }
    }

    private fun onProfileUpdated(): (UpdateProfileResponse) -> Unit = { profile ->
        this.profile.set(PlayerProfile(profile.profile))
    }

    private fun onProfileSuccess(): (FetchProfileResponse) -> Unit = { response ->
        val teamProfile = if (response.profiles.size <= 1) {
            response.profiles.firstOrNull()
        } else {
            Profile(response.profiles.joinToString("&") { it.name },
                0,
                "team${response.profiles.size}",
                PlayerPrefs(-1))
        }

        this.numberOfProfiles.set(response.profiles.size)
        teamProfile?.let {
            this.profile.set(PlayerProfile(it))
        }
    }

    private fun onProfileFailed(): (Throwable) -> Unit = {
        this.errorMsg.set(R.string.err_unable_to_fetch_players)
    }

    private fun onStatsSuccess(): (FetchProfileStatResponse) -> Unit = { response ->
        val teamStats = if (response.stats.size <= 1) {
            response.stats[0]
        } else {
            ProfileStat(
                response.stats.sumOf { it.numberOfGames },
                response.stats.sumOf { it.numberOfWins },
                response.stats.sumOf { it.numberOfDarts },
                response.stats.sumOf { it.numberOfPoints },
                response.stats.sumOf { it.numberOfDarts9 },
                response.stats.sumOf { it.numberOfPoints9 },
                response.stats.sumOf { it.numberOf180s },
                response.stats.sumOf { it.numberOf140s },
                response.stats.sumOf { it.numberOf100s },
                response.stats.sumOf { it.numberOf60s },
                response.stats.sumOf { it.numberOf20s },
                response.stats.sumOf { it.numberOf0s },
                response.stats.sumOf { it.numberOfDartsAtFinish },
                response.stats.sumOf { it.numberOfFinishes }
            )
        }

        this.stats.set(PlayerStats(teamStats))
    }

    private fun onStatsFailed(): (Throwable) -> Unit = {
        this.errorMsg.set(R.string.err_unable_to_fetch_stats)
    }
}
