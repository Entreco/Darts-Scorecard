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
class PlayerAdapter @Inject constructor(
        private val editor: PlayerEditor
) : TestableAdapter<SelectPlayerView>(), PlayerEditor.Callback {

    private val items = mutableListOf<PlayerViewModel>()
    private val teams = mutableListOf<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectPlayerView {
        val inflater = LazyInflater(parent.context).inflater
        val binding = DataBindingUtil.inflate<SelectPlayerViewBinding>(inflater, R.layout.select_player_view, parent, false)
        return SelectPlayerView(binding)
    }

    @Synchronized
    override fun onBindViewHolder(holder: SelectPlayerView, position: Int) {
        holder.bind(items[position], editor, teams, players(), bots(), position)
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
        val viewModel = PlayerViewModel(playerId, items.size + 1, playerName, true)
        items.add(viewModel)
        updateTeamCount()
        tryNotifyItemInserted(itemCount)
    }

    override fun onBotAdded(botName: String, botId: Long) {
        val viewModel = PlayerViewModel(botId, items.size + 1, botName, false)
        items.add(viewModel)
        updateTeamCount()
        tryNotifyItemInserted(itemCount)
    }

    fun participants(): List<PlayerViewModel> {
        return items
    }

    fun players(): List<Long> {
        return items.filter { it.isHuman.get() }.map { it.playerId.get() }
    }

    fun bots(): List<Long> {
        return items.filterNot { it.isHuman.get() }.map { it.playerId.get() }
    }

    @Synchronized
    private fun updateTeamCount() {
        teams.clear()
        teams += 1..itemCount
        tryNotifyItemRangeInserted(0, itemCount)
    }
}
