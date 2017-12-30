package nl.entreco.dartsscorecard.setup.players

import nl.entreco.dartsscorecard.databinding.SelectPlayerViewBinding

/**
 * Created by Entreco on 30/12/2017.
 */
class SelectPlayerView(private val binding: SelectPlayerViewBinding) : BasePlayerView<PlayerViewModel>(binding) {
    fun bind(viewModel: PlayerViewModel) {
        binding.player = viewModel
        binding.executePendingBindings()
    }
}