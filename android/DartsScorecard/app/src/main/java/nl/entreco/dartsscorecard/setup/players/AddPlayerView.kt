package nl.entreco.dartsscorecard.setup.players

import nl.entreco.dartsscorecard.databinding.AddPlayerViewBinding

/**
 * Created by Entreco on 30/12/2017.
 */
class AddPlayerView(private val binding: AddPlayerViewBinding) : BasePlayerView<AddPlayerClicker>(binding) {
    fun bind(clicker: AddPlayerClicker) {
        binding.clicker = clicker
        binding.executePendingBindings()
    }
}