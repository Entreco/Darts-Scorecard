package nl.entreco.dartsscorecard.play.live

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import nl.entreco.dartsscorecard.databinding.WidgetListStatsBinding

/**
 * Created by entreco on 24/03/2018.
 */
class LiveStatTransformer(size: Float) : ViewPager.PageTransformer {

    private val animator = LiveStatPageAnimator(size)

    override fun transformPage(page: View, position: Float) {
        when {
            position < -1 -> page.alpha = 0F
            position > 1 -> page.alpha = 0F
            else -> transform(page, position)
        }
    }

    private fun transform(page: View, position: Float) {
        val binding = DataBindingUtil.getBinding<WidgetListStatsBinding>(page)!!
        animator.transform(binding, page, position)
    }
}