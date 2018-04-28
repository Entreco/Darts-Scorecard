package nl.entreco.dartsscorecard.play.live

import android.databinding.BindingAdapter
import android.support.v4.view.ViewPager
import android.view.Gravity
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
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
            val margin = if (stats.size < 2) view.context.resources.getDimensionPixelOffset(R.dimen.xxlarge) else 0
            val lp = (view.layoutParams as RelativeLayout.LayoutParams)
            lp.setMargins(margin, 0, margin, 0)
            view.layoutParams = lp
        }

        @JvmStatic
        @BindingAdapter("hideIfOneTeam")
        fun hideIfOnlyOneTeam(view: View, hide: Boolean) {
            view.visibility = if (hide) View.GONE else View.VISIBLE
        }

        @JvmStatic
        @BindingAdapter("centerIfOneTeam")
        fun centerIfOnlyOneTeam(view: TextView, oneTeam: Boolean) {
            view.gravity = if(oneTeam) Gravity.CENTER else Gravity.END
        }

        @JvmStatic
        @BindingAdapter("currentTeam")
        fun scrollToCurrentTeam(pager: ViewPager, currentTeamIndex: Int) {
            pager.setCurrentItem(currentTeamIndex, true)
        }
    }

}