package nl.entreco.dartsscorecard.setup.players

import android.support.v7.widget.RecyclerView
import nl.entreco.dartsscorecard.databinding.SelectPlayerViewBinding
import nl.entreco.dartsscorecard.setup.Setup01Navigator

/**
 * Created by Entreco on 30/12/2017.
 */
class SelectPlayerView(private val binding: SelectPlayerViewBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(viewModel: PlayerViewModel, navigator: Setup01Navigator, entries: MutableList<Int>) {
        binding.player = viewModel
        binding.navigator = navigator
        binding.entries = entries
        binding.executePendingBindings()
    }
}