package nl.entreco.dartsscorecard.profile.view

import javax.inject.Inject


/**
 * Created by entreco on 27/02/2018.
 */
class ProfileNavigator @Inject constructor(private val activity: ProfileActivity) {

    fun onChooseImage(profile: PlayerProfile) {
        ProfileActivity.selectImage(activity)
    }

    fun onEditProfile(profile: PlayerProfile) {
        ProfileActivity.selectName(activity, profile.name.get(), profile.fav.get())
    }
}
