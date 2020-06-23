package nl.entreco.dartsscorecard.hiscores

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import nl.entreco.dartsscorecard.di.hiscore.HiScoreScope
import nl.entreco.dartsscorecard.profile.view.ProfileActivity
import javax.inject.Inject

class HiScoreNavigator @Inject constructor(
        @HiScoreScope private val activity: FragmentActivity
) : Observer<HiScoreItemModel> {

    override fun onChanged(item: HiScoreItemModel?) {
        item?.let {
            val id = longArrayOf(item.id)
            ProfileActivity.launch(activity, id)
        }
    }
}