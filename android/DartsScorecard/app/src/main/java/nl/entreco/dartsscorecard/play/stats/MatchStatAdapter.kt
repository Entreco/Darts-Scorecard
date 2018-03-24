package nl.entreco.dartsscorecard.play.stats

import android.databinding.DataBindingUtil
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.databinding.WidgetListStatsBinding
import nl.entreco.dartsscorecard.play.Play01Navigator
import javax.inject.Inject

/**
 * Created by entreco on 24/03/2018.
 */
class MatchStatAdapter @Inject constructor(private val navigator: Play01Navigator) : PagerAdapter() {

    private val items: MutableList<TeamStatModel> = mutableListOf()

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val binding = DataBindingUtil.inflate<WidgetListStatsBinding>(LayoutInflater.from(container.context), R.layout.widget_list_stats, container, false)
        binding.team0 = items[position]
        binding.team1 = items[(position + 1) % items.size]
        binding.navigator = navigator

        container.addView(binding.root)

        return binding.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, view: Any) {
        container.removeView(view as View)
    }


    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }

    override fun getCount(): Int {
        return items.size
    }

    fun populate(stats: Map<Int, TeamStatModel>) {
        items.clear()
        items.addAll(stats.map { it.value })
        notifyDataSetChanged()
    }
}