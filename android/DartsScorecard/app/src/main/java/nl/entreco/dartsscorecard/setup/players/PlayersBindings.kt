package nl.entreco.dartsscorecard.setup.players

import android.databinding.BindingAdapter
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

/**
 * Created by Entreco on 30/12/2017.
 */
class PlayersBindings {
    companion object {
        @JvmStatic
        @BindingAdapter("adapter")
        fun setAdapter(view: RecyclerView, adapter: PlayerAdapter) {
            view.layoutManager = LinearLayoutManager(view.context)
            view.itemAnimator = DefaultItemAnimator()
            view.adapter = adapter
        }
    }
}