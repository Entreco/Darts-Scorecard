package nl.entreco.dartsscorecard.setup.players

import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Entreco on 31/12/2017.
 */
class PlayersObserver(private val callback: Callback) : androidx.recyclerview.widget.RecyclerView.AdapterDataObserver() {
    interface Callback {
        fun onPlayersInserted(positionStart: Int, itemCount: Int)
    }

    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
        super.onItemRangeInserted(positionStart, itemCount)
        callback.onPlayersInserted(positionStart, itemCount)
    }
}