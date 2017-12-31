package nl.entreco.dartsscorecard.setup.players

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.databinding.SelectPlayerViewBinding
import javax.inject.Inject

/**
 * Created by Entreco on 30/12/2017.
 */
class PlayerAdapter @Inject constructor() : RecyclerView.Adapter<SelectPlayerView>(), AddPlayerClicker {

    private val items = mutableListOf<PlayerViewModel>()

    private class LazyInflater(context: Context) {
        val inflater: LayoutInflater by lazy { LayoutInflater.from(context) }
    }

    init {
        addPlayerNumber(0)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SelectPlayerView {
        return createPlayer(parent)
    }

    private fun createPlayer(parent: ViewGroup?): SelectPlayerView {
        val inflater = LazyInflater(parent?.context!!).inflater
        val binding = DataBindingUtil.inflate<SelectPlayerViewBinding>(inflater, R.layout.select_player_view, parent, false)
        return SelectPlayerView(binding)
    }

    override fun onBindViewHolder(holder: SelectPlayerView?, position: Int) {
        holder?.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onAddPlayer() {
        updateTeamCount()
        addPlayerNumber(itemCount)
    }

    private fun addPlayerNumber(index: Int) {
        items.add(PlayerViewModel(index))
        notifyItemInserted(itemCount)
    }

    private fun updateTeamCount() {
        // Update TeamCounter for all
        items.forEach {
            it.onTeamsUpdated(itemCount)
        }
        notifyItemRangeChanged(0, itemCount)
    }
}