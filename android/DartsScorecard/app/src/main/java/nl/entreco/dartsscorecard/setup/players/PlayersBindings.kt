package nl.entreco.dartsscorecard.setup.players

import android.databinding.BindingAdapter

/**
 * Created by Entreco on 30/12/2017.
 */
abstract class PlayersBindings {
    companion object {
        @JvmStatic
        @BindingAdapter("adapter")
        fun setAdapter(view: PlayersWidget, adapter: PlayerAdapter) {
            view.adapter = adapter
        }
    }
}