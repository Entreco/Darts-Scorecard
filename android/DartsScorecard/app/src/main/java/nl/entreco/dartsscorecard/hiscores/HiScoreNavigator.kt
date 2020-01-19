package nl.entreco.dartsscorecard.hiscores

import android.app.Activity
import nl.entreco.dartsscorecard.profile.view.ProfileActivity

class HiScoreNavigator(private val activity: Activity) {

    fun onProfileSelected(item: HiScoreItemModel) {
        val id = longArrayOf(item.id)
        ProfileActivity.launch(activity, id)
    }
}