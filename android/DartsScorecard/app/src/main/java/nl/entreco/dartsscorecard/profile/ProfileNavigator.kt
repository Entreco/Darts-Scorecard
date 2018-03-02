package nl.entreco.dartsscorecard.profile

import javax.inject.Inject


/**
 * Created by entreco on 27/02/2018.
 */
class ProfileNavigator @Inject constructor(private val activity: ProfileActivity) {

    fun onChooseImage(profile: PlayerProfile) {
        ProfileActivity.selectImage(activity)
    }

    fun onChangeName(profile: PlayerProfile) {

    }

    fun onChangeFavouriteDouble(profile: PlayerProfile){

    }
}
