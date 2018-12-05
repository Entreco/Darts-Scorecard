package nl.entreco.dartsscorecard.hiscores

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class HiScorePager(fm: FragmentManager?) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return HiScoreFragment.instance(position)
    }

    override fun getCount() = 6
}