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

    override fun onAddPlayer(): String {
        updateTeamCount()
        return addPlayerNumber(itemCount)
    }

    private fun addPlayerNumber(index: Int): String {
        val pvm = PlayerViewModel(index)
        items.add(pvm)
        tryNotifyItemInserted(itemCount)
        return pvm.name.get()
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

    fun replacePlayer(oldPlayerName: String, newPlayerName: String) {
        val pvm = items.first { it.name.get() == oldPlayerName }
        val index = items.indexOf(pvm)
        pvm.name.set(newPlayerName)
        tryNotifyItemChanged(index)
    }
}