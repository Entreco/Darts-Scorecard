package nl.entreco.dartsscorecard.beta.donate

import android.databinding.BindingAdapter
import android.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.databinding.DonateItemBinding
import nl.entreco.domain.beta.Donation

/**
 * Created by entreco on 08/02/2018.
 */
class DonateBindings {

    companion object {

        @JvmStatic
        @BindingAdapter("donations")
        fun addDonations(viewGroup: ViewGroup, donations: List<Donation>?) {
            val inflater = LayoutInflater.from(viewGroup.context)
            clearPreviousViews(viewGroup)
            addNewViews(donations, inflater, viewGroup)
        }

        private fun addNewViews(donations: List<Donation>?, inflater: LayoutInflater?, viewGroup: ViewGroup) {
            donations?.forEach { donation ->
                val binding = DataBindingUtil.inflate<DonateItemBinding>(inflater, R.layout.donate_item, viewGroup, true)
                binding.donation = donation
            }
        }

        internal fun clearPreviousViews(viewGroup: ViewGroup) {
            (1 until viewGroup.childCount).forEach { viewGroup.removeViewAt(it) }
        }
    }
}