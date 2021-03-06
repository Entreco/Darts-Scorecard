package nl.entreco.dartsscorecard.beta

import androidx.recyclerview.widget.DiffUtil
import nl.entreco.domain.beta.Feature

class BetaDiffCalculator(
        private val old: List<Feature>,
        private val new: List<Feature>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return old[oldItemPosition].ref == new[newItemPosition].ref
    }

    override fun getOldListSize(): Int {
        return old.size
    }

    override fun getNewListSize(): Int {
        return new.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return old[oldItemPosition] == new[newItemPosition]
    }
}