package nl.entreco.dartsscorecard.setup.players

import androidx.databinding.DataBindingUtil
import android.view.ViewGroup
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.base.TestableAdapter
import nl.entreco.dartsscorecard.databinding.SelectPlayerViewBinding
import javax.inject.Inject

/**
 * Created by Entreco on 30/12/2017.
 */
class PlayerAdapter @Inject constructor(private val editor: PlayerEditor) : TestableAdapter<SelectPlayerView>(), PlayerEditor.Callback {

    private val items = mutableListOf<PlayerViewModel>()
    private val teams = mutableListOf<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectPlayerView {
        val inflater = LazyInflater(parent.context).inflater
        val binding = DataBindingUtil.inflate<SelectPlayerViewBinding>(inflater, R.layout.select_player_view, parent, false)
        return SelectPlayerView(binding)
    }

    @Synchronized
    override fun onBindViewHolder(holder: SelectPlayerView, position: Int) {
        holder.bind(items[position], editor, teams, entries(), position)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onPlayerEdited(position: Int, teamIndex: Int, playerName: String, playerId: Long) {
        val viewModel = items[position]
        viewModel.playerId.set(playerId)
        viewModel.name.set(playerName)
        viewModel.teamIndex.set(teamIndex)
        tryNotifyItemChanged(position)
    }

    override fun onPlayerAdded(playerName: String, playerId: Long) {
        val viewModel = PlayerViewModel(playerId, items.size + 1, playerName)
        items.add(viewModel)
        updateTeamCount()
        tryNotifyItemInserted(itemCount)
    }

    fun playersMap(): Array<PlayerViewModel> {
        return items.toTypedArray()
    }

    fun entries(): List<Long> {
        return items.map { it.playerId.get() }
    }

    @Synchronized
    private fun updateTeamCount() {
        teams.clear()
        teams += 1..itemCount
        tryNotifyItemRangeInserted(0, itemCount)
    }
}
