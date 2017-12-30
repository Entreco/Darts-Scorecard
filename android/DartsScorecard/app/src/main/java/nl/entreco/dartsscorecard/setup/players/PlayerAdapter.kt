package nl.entreco.dartsscorecard.setup.players

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.databinding.AddPlayerViewBinding
import nl.entreco.dartsscorecard.databinding.SelectPlayerViewBinding
import javax.inject.Inject

/**
 * Created by Entreco on 30/12/2017.
 */
class PlayerAdapter @Inject constructor() : RecyclerView.Adapter<BasePlayerView<*>>(), AddPlayerClicker {

    private val typeAdd = 0
    private val typePlayer = 1

    private val items = mutableListOf<PlayerViewModel>()

    private class LazyInflater(context: Context) {
        val inflater: LayoutInflater by lazy { LayoutInflater.from(context) }
    }

    init {
        addPlayerNumber(1)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BasePlayerView<*> {
        return when (viewType) {
            typeAdd -> createAddFooter(parent)
            else -> createPlayer(parent)
        }
    }

    private fun createAddFooter(parent: ViewGroup?): AddPlayerView {
        val inflater = LazyInflater(parent?.context!!).inflater
        val binding = DataBindingUtil.inflate<AddPlayerViewBinding>(inflater, R.layout.add_player_view, parent, false)
        return AddPlayerView(binding)
    }

    private fun createPlayer(parent: ViewGroup?): SelectPlayerView {
        val inflater = LazyInflater(parent?.context!!).inflater
        val binding = DataBindingUtil.inflate<SelectPlayerViewBinding>(inflater, R.layout.select_player_view, parent, false)
        return SelectPlayerView(binding)
    }

    override fun onBindViewHolder(holder: BasePlayerView<*>?, position: Int) {
        when (holder) {
            is SelectPlayerView -> holder.bind(items[position])
            is AddPlayerView -> holder.bind(this)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(position + 1 == itemCount) typeAdd
        else typePlayer
    }

    override fun getItemCount(): Int {
        return items.size + 1
    }

    override fun onAddPlayer() {
        updateTeamCount()
        addPlayerNumber(items.size + 1)
    }

    private fun addPlayerNumber(number: Int) {
        items.add(PlayerViewModel(number, number))
        notifyItemInserted(items.size)
    }

    private fun updateTeamCount() {
        // Update TeamCounter for all
        items.forEach {
            it.updateTeams(items.size + 1)
        }
        notifyItemRangeChanged(0, itemCount)
    }
}