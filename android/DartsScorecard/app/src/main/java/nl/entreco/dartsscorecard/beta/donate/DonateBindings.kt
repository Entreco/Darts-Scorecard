package nl.entreco.dartsscorecard.beta.donate

import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.databinding.DonateItemBinding
import nl.entreco.domain.beta.Donation

/**
 * Created by entreco on 08/02/2018.
 */

object DonateBindings {

    @JvmStatic
    @BindingAdapter("donations", "viewModel")
    fun addDonations(viewGroup: ViewGroup, donations: List<Donation>?, viewModel: DonateViewModel?) {
        val inflater = LayoutInflater.from(viewGroup.context)
        clearPreviousViewsIfEmpty(viewGroup)
        addNewViews(donations, viewModel, inflater, viewGroup)
    }

    private fun addNewViews(donations: List<Donation>?, viewModel: DonateViewModel?, inflater: LayoutInflater, viewGroup: ViewGroup) {
        if (viewModel != null) {
            donations?.forEach { donation ->
                val binding = DataBindingUtil.inflate<DonateItemBinding>(inflater, R.layout.donate_item, viewGroup, true)
                binding.donation = donation
                binding.viewModel = viewModel
            }
        }
    }

    internal fun clearPreviousViewsIfEmpty(viewGroup: ViewGroup) {
        viewGroup.removeAllViews()
    }
}