package nl.entreco.dartsscorecard.setup.players

import android.support.v7.widget.RecyclerView
import nl.entreco.dartsscorecard.databinding.SelectPlayerViewBinding

/**
 * Created by Entreco on 30/12/2017.
 */
class SelectPlayerView(private val binding: SelectPlayerViewBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(viewModel: PlayerViewModel, editor: PlayerEditor, entries: MutableList<Int>, positionInList: Int) {
        binding.player = viewModel
        binding.editor = editor
        binding.entries = entries
        binding.positionInList = positionInList
        binding.executePendingBindings()
    }
}