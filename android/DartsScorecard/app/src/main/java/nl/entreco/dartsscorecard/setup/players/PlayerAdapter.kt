package nl.entreco.dartsscorecard.setup.players

import android.databinding.DataBindingUtil
import android.view.ViewGroup
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.base.TestableAdapter
import nl.entreco.dartsscorecard.databinding.SelectPlayerViewBinding
import nl.entreco.dartsscorecard.setup.Setup01Navigator
import javax.inject.Inject

/**
 * Created by Entreco on 30/12/2017.
 */
class PlayerAdapter @Inject constructor(private val navigator: Setup01Navigator) : TestableAdapter<SelectPlayerView>(), AddPlayerClicker {

    private val items = mutableListOf<PlayerViewModel>()

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
        holder?.bind(items[position], navigator)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onAddPlayer() {
        addPlayerNumber(itemCount)
        updateTeamCount()
    }

    private fun addPlayerNumber(index: Int) {
        items.add(PlayerViewModel(index))
        tryNotifyItemInserted(itemCount)
    }

    private fun updateTeamCount() {
        items.forEach {
            it.onTeamsUpdated(itemCount)
        }
        tryNotifyItemRangeChanged(0, itemCount)
    }

    fun playersMap(): Array<PlayerViewModel> {
        return items.toTypedArray()
    }

    fun replacePlayer(oldName: String?, playerName: String?, playerId: Long?) {
        val pvm = items.first { it.name.get() == oldName }
        val index = items.indexOf(pvm)
        pvm.name.set(playerName)
        tryNotifyItemChanged(index)
    }
}