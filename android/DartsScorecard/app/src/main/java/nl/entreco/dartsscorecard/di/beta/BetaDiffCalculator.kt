package nl.entreco.dartsscorecard.di.beta

import android.support.v7.util.DiffUtil
import nl.entreco.domain.beta.Feature

class BetaDiffCalculator(private val old: List<Feature>, private val new: List<Feature>) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return old[oldItemPosition] == new[newItemPosition]
    }

    override fun getOldListSize(): Int {
        return old.size
    }

    override fun getNewListSize(): Int {
        return new.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = old[oldItemPosition]
        val newItem = new[newItemPosition]
        return oldItem.description == newItem.description &&
                oldItem.image == newItem.image &&
                oldItem.required == newItem.required &&
                oldItem.title == newItem.title
    }
}