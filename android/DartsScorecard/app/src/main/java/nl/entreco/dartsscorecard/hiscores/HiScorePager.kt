package nl.entreco.dartsscorecard.hiscores

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import nl.entreco.dartsscorecard.R
import nl.entreco.shared.scopes.ActivityScope
import nl.entreco.domain.hiscores.HiScoreItem
import javax.inject.Inject

class HiScorePager @Inject constructor(
        @ActivityScope private val context: Context,
        @ActivityScope fm: FragmentManager
) : FragmentPagerAdapter(fm) {

    private val items = mutableListOf<HiScoreItem>()

    override fun getItem(position: Int): Fragment {
        return HiScoreFragment.instance(position)
    }

    override fun getCount() = items.size

    override fun getPageTitle(position: Int): CharSequence? {
        return when (items[position]) {
            is HiScoreItem.OverallAvg -> context.getString(R.string.hiscore_title_overall_average)
            is HiScoreItem.ScoringAvg -> context.getString(R.string.hiscore_title_first9_average)
            is HiScoreItem.CheckoutPerc -> context.getString(R.string.hiscore_title_checkout_percentage)
            is HiScoreItem.WinRatio -> context.getString(R.string.hiscore_title_games_ratio)
            is HiScoreItem.Num180 -> context.getString(R.string.hiscore_title_num_180)
            is HiScoreItem.Num140 -> context.getString(R.string.hiscore_title_num_140)
            is HiScoreItem.Num100 -> context.getString(R.string.hiscore_title_num_100)
            is HiScoreItem.Num60 -> context.getString(R.string.hiscore_title_num_60)
            is HiScoreItem.Num20 -> context.getString(R.string.hiscore_title_num_20)
            is HiScoreItem.NumBust -> context.getString(R.string.hiscore_title_num_0)
            is HiScoreItem.BestMatchAvg -> context.getString(R.string.hiscore_title_best_avg)
            is HiScoreItem.BestMatchCheckout -> context.getString(R.string.hiscore_title_best_co)
            is HiScoreItem.HighestScore -> context.getString(R.string.hiscore_title_highest_score)
            is HiScoreItem.HighestCheckout -> context.getString(R.string.hiscore_title_highest_co)
            else -> ""
        }
    }

    fun show(hiScores: List<HiScoreItem>) {
        items.clear()
        items.addAll(hiScores)
        notifyDataSetChanged()
    }
}