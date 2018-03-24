package nl.entreco.dartsscorecard.play.stats

import android.databinding.BindingAdapter
import android.support.v4.view.ViewPager

/**
 * Created by entreco on 24/03/2018.
 */
class MatchStatBinding {

    companion object {
        @JvmStatic
        @BindingAdapter("stats", "adapter")
        fun setupViewPager(view: ViewPager, stats: Map<Int, TeamStatModel>, adapter: MatchStatAdapter) {
            view.setPageTransformer(true, MatchStatTransformer())
            adapter.populate(stats)
            view.adapter = adapter
            view.currentItem = 0
        }
    }
}