package nl.entreco.dartsscorecard.profile.select

import android.app.Activity
import android.support.v4.app.ActivityOptionsCompat
import android.view.View
import nl.entreco.dartsscorecard.profile.edit.EditPlayerNameActivity
import nl.entreco.dartsscorecard.profile.view.ProfileActivity
import javax.inject.Inject

/**
 * Created by entreco on 05/03/2018.
 */
class SelectProfileNavigator @Inject constructor(private val activity: Activity) {

    fun onProfileSelected(view: View, profile: ProfileModel) {
        val id = longArrayOf(profile.id)
        ProfileActivity.launch(activity, view, id)
    }

    fun onCreateProfile(view: View){
        val intent = EditPlayerNameActivity.launch(activity)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, view, view.transitionName)
        activity.startActivityForResult(intent, SelectProfileActivity.REQUEST_CODE_CREATE, options.toBundle())
    }
}
