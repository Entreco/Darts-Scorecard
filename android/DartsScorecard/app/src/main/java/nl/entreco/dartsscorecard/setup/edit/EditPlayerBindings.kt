package nl.entreco.dartsscorecard.setup.edit

import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.databinding.ExistingPlayerViewBinding
import nl.entreco.domain.model.players.Player

/**
 * Created by Entreco on 02/01/2018.
 */
object EditPlayerBindings {

    @JvmStatic
    @BindingAdapter("suggestion")
    fun selectAll(view: TextView, suggestedName: String) {
        view.text = suggestedName
        view.setSelectAllOnFocus(true)
        view.clearFocus()
        view.requestFocusFromTouch()
    }

    @JvmStatic
    @BindingAdapter("filteredPlayers", "clicker")
    fun showFilteredPlayers(view: ViewGroup, players: List<Player>, clicker: ExistingPlayerSelectedClicker) {
        view.removeAllViews()
        players.forEach {
            val binding = DataBindingUtil.inflate<ExistingPlayerViewBinding>(LayoutInflater.from(view.context), R.layout.existing_player_view, view, false)
            bind(binding, it, clicker, view)
        }
    }

    @JvmStatic
    @BindingAdapter("availableBots", "clicker")
    fun showAvailableBots(view: ViewGroup, players: List<Player>, clicker: ExistingPlayerSelectedClicker) {
        view.removeAllViews()
        players.forEach {
            val binding = DataBindingUtil.inflate<ExistingPlayerViewBinding>(LayoutInflater.from(view.context), R.layout.existing_player_view, view, false)
            bind(binding, it, clicker, view)
        }
    }

    internal fun bind(binding: ExistingPlayerViewBinding, player: Player, clicker: ExistingPlayerSelectedClicker, view: ViewGroup) {
        binding.player = player
        binding.clicker = clicker
        view.addView(binding.root)
    }
}