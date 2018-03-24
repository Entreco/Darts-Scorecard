package nl.entreco.dartsscorecard.play.stats

import android.databinding.DataBindingUtil
import android.support.v4.view.ViewPager
import android.view.View
import nl.entreco.dartsscorecard.databinding.WidgetListStatsBinding

/**
 * Created by entreco on 24/03/2018.
 */
class MatchStatTransformer : ViewPager.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        when {
            position < -1 -> page.alpha = 0F
            position > 1 -> page.alpha = 0F
            else -> transform(page, position)
        }
    }

    private fun transform(page: View, position: Float) {
        val binding = DataBindingUtil.getBinding<WidgetListStatsBinding>(page)
        MatchStatAnimator(binding).transform(position, page.width)
    }
}