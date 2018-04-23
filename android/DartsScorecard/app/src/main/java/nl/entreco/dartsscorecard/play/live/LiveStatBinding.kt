package nl.entreco.dartsscorecard.play.live

import android.databinding.BindingAdapter
import android.support.v4.view.ViewPager
import android.view.View
import android.widget.FrameLayout
import nl.entreco.dartsscorecard.R

/**
 * Created by entreco on 24/03/2018.
 */
class LiveStatBinding {

    companion object {
        @JvmStatic
        @BindingAdapter("liveStats", "adapter")
        fun setupViewPager(view: ViewPager, stats: Map<Int, TeamLiveStatModel>, adapter: LiveStatAdapter) {
            adapter.populate(stats)
            view.setPageTransformer(false, LiveStatTransformer(view.context.resources.getDimension(R.dimen.match_stat_width)))
            view.adapter = adapter
            view.setCurrentItem(0, true)
        }

        @JvmStatic
        @BindingAdapter("hideIfOneTeam")
        fun hideIfOnlyOneTeam(view: View, hide: Boolean) {
            view.visibility = if(hide) View.GONE else View.VISIBLE
        }

        @JvmStatic
        @BindingAdapter("bigMarginIfOneTeam")
        fun bigMarginIfOneTeam(view: View, big: Boolean) {
            val lporig = view.layoutParams
            val lp : FrameLayout.LayoutParams = view.layoutParams as FrameLayout.LayoutParams
            lp.marginStart = if(big) view.resources.getDimension(R.dimen.xxlarge).toInt() else 0
            lp.marginEnd = if(big) view.resources.getDimension(R.dimen.xxlarge).toInt() else 0

        }

        @JvmStatic
        @BindingAdapter("currentTeam")
        fun scrollToCurrentTeam(pager: ViewPager, currentTeamIndex: Int) {
            pager.setCurrentItem(currentTeamIndex, true)
        }
    }

}