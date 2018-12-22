package nl.entreco.dartsscorecard.play.live

import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.databinding.WidgetListStatsBinding
import nl.entreco.dartsscorecard.play.Play01Navigator
import javax.inject.Inject
import kotlin.math.max

/**
 * Created by entreco on 24/03/2018.
 */
class LiveStatAdapter @Inject constructor(private val navigator: Play01Navigator) : PagerAdapter() {

    private val items: MutableList<TeamLiveStatModel> = mutableListOf()

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val binding = DataBindingUtil.inflate<WidgetListStatsBinding>(LayoutInflater.from(container.context), R.layout.widget_list_stats, container, false)
        binding.team0 = items[position]
        binding.team1 = if(items.size >= 2) items[(position + 1) % items.size] else null
        binding.navigator = navigator
        binding.root.tag = position
        binding.executePendingBindings()

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
        return if(items.size == 1) 1 else return max(0, items.size - 1)
    }

    fun populate(stats: Map<Int, TeamLiveStatModel>) {
        items.clear()
        items.addAll(stats.map { it.value })
        notifyDataSetChanged()
    }
}