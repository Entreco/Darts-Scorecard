package nl.entreco.dartsscorecard.profile.select

import android.view.View
import nl.entreco.dartsscorecard.profile.view.ProfileActivity
import javax.inject.Inject

/**
 * Created by entreco on 05/03/2018.
 */
class SelectProfileNavigator @Inject constructor(private val activity: SelectProfileActivity) {

    fun onProfileSelected(view: View, profile: ProfileModel) {
        val id = longArrayOf(profile.id)
        ProfileActivity.launch(activity, view, id)
    }
}
