package nl.entreco.dartsscorecard.setup.players

import android.databinding.DataBindingUtil
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

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SelectPlayerView {
        val inflater = LazyInflater(parent?.context!!).inflater
        val binding = DataBindingUtil.inflate<SelectPlayerViewBinding>(inflater, R.layout.select_player_view, parent, false)
        return SelectPlayerView(binding)
    }

    override fun onBindViewHolder(holder: SelectPlayerView?, position: Int) {
        holder?.bind(items[position], editor, teams, position)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onPlayerEdited(position: Int, teamIndex: Int, playerName: String) {
        val viewModel = items[position]
        viewModel.name.set(playerName)
        viewModel.teamIndex.set(teamIndex)
        tryNotifyItemChanged(position)
        updateTeamCount()
    }

    override fun onPlayerAdded(teamIndex: Int, playerName: String) {
        val viewModel = PlayerViewModel(teamIndex, playerName)
        items.add(viewModel)
        tryNotifyItemInserted(itemCount)
        updateTeamCount()
    }

    fun playersMap(): Array<PlayerViewModel> {
        return items.toTypedArray()
    }

    private fun updateTeamCount() {
        teams.clear()
        teams += 1..itemCount
        tryNotifyItemRangeChanged(0, itemCount)
    }
}