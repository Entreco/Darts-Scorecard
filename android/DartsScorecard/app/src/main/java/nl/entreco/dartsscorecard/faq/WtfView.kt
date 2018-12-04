package nl.entreco.dartsscorecard.faq

import androidx.recyclerview.widget.RecyclerView
import nl.entreco.dartsscorecard.databinding.WtfViewBinding
import nl.entreco.domain.wtf.WtfItem

class WtfView(private val binding: WtfViewBinding) : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {
    fun bind(item: WtfItem, collapsed: Boolean, toggler: WtfToggler) {
        binding.wtf = WtfModel(item, toggler, collapsed)
        binding.executePendingBindings()
    }
}