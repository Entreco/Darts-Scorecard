package nl.entreco.dartsscorecard.setup.players

import androidx.recyclerview.widget.RecyclerView
import nl.entreco.dartsscorecard.databinding.SelectPlayerViewBinding

/**
 * Created by Entreco on 30/12/2017.
 */
class SelectPlayerView(private val binding: SelectPlayerViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
    fun bind(viewModel: PlayerViewModel, editor: PlayerEditor, entries: List<Int>,
             others: List<Long>, positionInList: Int) {
        binding.player = viewModel
        binding.editor = editor
        binding.entries = entries
        binding.others = others
        binding.positionInList = positionInList
        binding.executePendingBindings()
    }
}