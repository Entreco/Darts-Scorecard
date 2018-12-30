package nl.entreco.dartsscorecard.play.live

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.databinding.WidgetSetStatBinding

/**
 * Created by entreco on 24/03/2018.
 */

object LiveStatBinding {
    @JvmStatic
    @BindingAdapter("liveStats", "adapter")
    fun setupViewPager(view: ViewPager, stats: Map<Int, TeamLiveStatModel>,
                       adapter: LiveStatAdapter) {
        adapter.populate(stats)
        view.setPageTransformer(false,
                LiveStatTransformer(view.context.resources.getDimension(R.dimen.match_stat_width)))
        view.adapter = adapter
        view.setCurrentItem(0, true)
        val margin = if (stats.size < 2) view.context.resources.getDimensionPixelOffset(
                R.dimen.xxlarge) else 0
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
        view.gravity = if (oneTeam) Gravity.CENTER else Gravity.END
    }

    @JvmStatic
    @BindingAdapter("currentTeam")
    fun scrollToCurrentTeam(pager: ViewPager, currentTeamIndex: Int) {
        pager.setCurrentItem(currentTeamIndex, true)
    }

    @JvmStatic
    @BindingAdapter("breakdown1", "breakdown2", requireAll = true)
    fun showSetBreakdown(view: ViewGroup, breakdown1: Map<Int, TeamSetStat>?,
                         breakdown2: Map<Int, TeamSetStat>?) {
        view.removeAllViews()
        val layoutInflater = LayoutInflater.from(view.context)
        if (breakdown1?.size ?: 0 >= breakdown2?.size ?: 0) {
            breakdown1?.forEach { setNumber, setStat ->
                val binding = DataBindingUtil.inflate<WidgetSetStatBinding>(layoutInflater,
                        R.layout.widget_set_stat, view, false)
                binding.number = setNumber + 1
                binding.team0 = setStat
                binding.team1 = breakdown2?.getOrDefault(setNumber, TeamSetStat(setNumber, "--", "0/0", "--"))
                view.addView(binding.root)
            }
        } else {
            breakdown2?.forEach { setNumber, setStat ->
                val binding = DataBindingUtil.inflate<WidgetSetStatBinding>(layoutInflater,
                        R.layout.widget_set_stat, view, false)
                binding.number = setNumber + 1
                binding.team0 = breakdown1?.getOrDefault(setNumber, TeamSetStat(setNumber, "--", "0/0", "--"))
                binding.team1 = setStat
                view.addView(binding.root)
            }
        }
    }
}
