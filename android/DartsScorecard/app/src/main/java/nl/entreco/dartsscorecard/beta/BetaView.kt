package nl.entreco.dartsscorecard.beta

import android.support.v7.widget.RecyclerView
import nl.entreco.dartsscorecard.databinding.BetaViewBinding
import nl.entreco.domain.beta.Feature

/**
 * Created by entreco on 03/02/2018.
 */
class BetaView(private val binding: BetaViewBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(feature: Feature, collapsible: Collapsible?) {
        binding.feature = BetaModel(feature)
        binding.collapser = collapsible
        binding.executePendingBindings()
    }
}