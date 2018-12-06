package nl.entreco.dartsscorecard.hiscores

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.databinding.HiscoreListItemBinding

class HiScoreAdapter : ListAdapter<Pair<String, String>, HiScoreViewHolder>(HiScoreDif()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HiScoreViewHolder {
        val binding = DataBindingUtil.inflate<HiscoreListItemBinding>(LayoutInflater.from(parent.context), R.layout.hiscore_list_item, parent, false)
        return HiScoreViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HiScoreViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class HiScoreDif : DiffUtil.ItemCallback<Pair<String, String>>() {
    override fun areItemsTheSame(oldItem: Pair<String, String>,
                                 newItem: Pair<String, String>): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Pair<String, String>,
                                    newItem: Pair<String, String>): Boolean {
        return oldItem == newItem
    }
}

class HiScoreViewHolder(private val binding: HiscoreListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Pair<String, String>?) {
        binding.name = item?.first
        binding.score = item?.second
        binding.executePendingBindings()
    }

}