package nl.entreco.dartsscorecard.wtf

import android.support.v7.util.DiffUtil
import nl.entreco.domain.wtf.WtfItem

class WtfDiffCalculator(private val old: List<WtfItem>, private val new: List<WtfItem>) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return old[oldItemPosition].docId == new[newItemPosition].docId
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