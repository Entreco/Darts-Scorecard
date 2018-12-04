package nl.entreco.dartsscorecard.setup.players

import androidx.databinding.BindingAdapter

/**
 * Created by Entreco on 30/12/2017.
 */
object PlayersBindings {
    @JvmStatic
    @BindingAdapter("adapter")
    fun setAdapter(view: PlayersWidget, adapter: PlayerAdapter) {
        view.adapter = adapter
    }
}