package nl.entreco.dartsscorecard.hiscores

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.databinding.HiscoreListItemBinding

class HiScoreAdapter : ListAdapter<String, HiScoreViewHolder>(HiScoreDif()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HiScoreViewHolder {
        val binding = DataBindingUtil.inflate<HiscoreListItemBinding>(LayoutInflater.from(parent.context), R.layout.hiscore_list_item, parent, false)
        return HiScoreViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HiScoreViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class HiScoreDif : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem === newItem
    }
}

class HiScoreViewHolder(private val binding: HiscoreListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
    fun bind(item: String?) {
        binding.name = item
        binding.remco = item
        binding.executePendingBindings()
    }

}