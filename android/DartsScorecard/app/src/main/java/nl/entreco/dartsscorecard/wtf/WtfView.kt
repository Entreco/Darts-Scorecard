package nl.entreco.dartsscorecard.wtf

import android.support.v7.widget.RecyclerView
import nl.entreco.dartsscorecard.databinding.WtfViewBinding
import nl.entreco.domain.wtf.WtfItem

class WtfView(private val binding: WtfViewBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: WtfItem, collapsed: Boolean, toggler: WtfToggler) {
        binding.wtf = WtfModel(item, toggler, collapsed)
        binding.executePendingBindings()
    }
}