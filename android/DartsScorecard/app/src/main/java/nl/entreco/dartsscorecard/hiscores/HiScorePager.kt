package nl.entreco.dartsscorecard.hiscores

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import nl.entreco.domain.hiscores.HiScoreItem

class HiScorePager(
        fm: FragmentManager
) : FragmentPagerAdapter(fm) {

    private val items = mutableListOf<HiScoreItem>()
    override fun getItem(position: Int): Fragment {
        return HiScoreFragment.instance(position)
    }

    override fun getCount() = items.size

    override fun getPageTitle(position: Int): CharSequence? {
        return when(items[position]){
            is HiScoreItem.OverallAverage -> "Avg."
            else -> ""
        }
    }

    fun show(hiScores: List<HiScoreItem>) {
        items.clear()
        items.addAll(hiScores)
        notifyDataSetChanged()
    }
}