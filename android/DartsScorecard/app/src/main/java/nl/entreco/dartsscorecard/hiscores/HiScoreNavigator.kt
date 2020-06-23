package nl.entreco.dartsscorecard.hiscores

import android.app.Activity
import androidx.lifecycle.Observer
import nl.entreco.dartsscorecard.profile.view.ProfileActivity

class HiScoreNavigator(
        private val activity: Activity
) : Observer<HiScoreItemModel> {

    override fun onChanged(item: HiScoreItemModel?) {
        item?.let {
            val id = longArrayOf(item.id)
            ProfileActivity.launch(activity, id)
        }
    }
}