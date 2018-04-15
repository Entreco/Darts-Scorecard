package nl.entreco.dartsscorecard.play.stats

import android.databinding.BindingAdapter
import android.support.v4.view.ViewPager
import android.view.View
import nl.entreco.dartsscorecard.R

/**
 * Created by entreco on 24/03/2018.
 */
class MatchStatBinding {

    companion object {
        @JvmStatic
        @BindingAdapter("stats", "adapter")
        fun setupViewPager(view: ViewPager, stats: Map<Int, TeamStatModel>, adapter: MatchStatAdapter) {
            adapter.populate(stats)
            view.setPageTransformer(false, MatchStatTransformer(view.context.resources.getDimension(R.dimen.match_stat_width)))
            view.adapter = adapter
            view.setCurrentItem(0, true)
        }

        @JvmStatic
        @BindingAdapter("hideIfOneTeam")
        fun hideIfOnlyOneTeam(view: View, hide: Boolean) {
            view.visibility = if(hide) View.GONE else View.VISIBLE
        }
    }

}