package nl.entreco.dartsscorecard.beta

import android.support.v7.widget.RecyclerView
import nl.entreco.dartsscorecard.databinding.BetaViewBinding

/**
 * Created by entreco on 30/01/2018.
 */
class BetaView(private val binding: BetaViewBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind() {
//        binding.viewModel = teamScoreViewModel
        binding.executePendingBindings()
    }
}