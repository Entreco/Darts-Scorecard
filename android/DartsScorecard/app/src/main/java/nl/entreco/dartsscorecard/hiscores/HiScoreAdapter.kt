package nl.entreco.dartsscorecard.hiscores

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.databinding.HiscoreListItemBinding
import javax.inject.Inject

class HiScoreAdapter : ListAdapter<HiScoreItemModel, HiScoreViewHolder>(HiScoreDif()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HiScoreViewHolder {
        val binding = DataBindingUtil.inflate<HiscoreListItemBinding>(
                LayoutInflater.from(parent.context), R.layout.hiscore_list_item, parent, false)
        return HiScoreViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HiScoreViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class HiScoreDif : DiffUtil.ItemCallback<HiScoreItemModel>() {

    override fun areItemsTheSame(
            oldItem: HiScoreItemModel,
            newItem: HiScoreItemModel
    ) = oldItem.id == newItem.id

    override fun areContentsTheSame(
            oldItem: HiScoreItemModel,
            newItem: HiScoreItemModel
    ) = oldItem == newItem
}

class HiScoreViewHolder(
        private val binding: HiscoreListItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: HiScoreItemModel) {
        binding.item = item
        binding.executePendingBindings()
    }
}