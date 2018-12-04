package nl.entreco.dartsscorecard.play.score

import androidx.databinding.DataBindingUtil
import android.view.ViewGroup
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.base.TestableAdapter
import nl.entreco.dartsscorecard.databinding.TeamScoreViewBinding
import nl.entreco.domain.model.Next
import nl.entreco.domain.model.Score
import nl.entreco.domain.model.Turn
import nl.entreco.domain.model.players.Player
import javax.inject.Inject

/**
 * Created by Entreco on 22/11/2017.
 */
class ScoreAdapter @Inject constructor() : TestableAdapter<TeamScoreView>() {

    private val items = mutableListOf<TeamScoreViewModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamScoreView {
        val inflater = LazyInflater(parent.context).inflater
        val binding = DataBindingUtil.inflate<TeamScoreViewBinding>(inflater, R.layout.team_score_view, parent, false)
        return TeamScoreView(binding)
    }

    override fun onBindViewHolder(holder: TeamScoreView, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun addItem(teamScoreViewModel: TeamScoreViewModel) {
        items.add(teamScoreViewModel)
        tryNotifyItemInserted(items.size - 1)
    }

    fun teamAtIndexScored(position: Int, score: Score, by: Player) {
        items[position].scored(score, by)
        tryNotifyItemChanged(position)
    }

    fun teamAtIndexThrew(position: Int, turn: Turn, player: Player) {
        items[position].threw(turn, player)
        tryNotifyItemChanged(position)
    }

    fun teamAtIndexTurnUpdate(position: Int, next: Next) {
        if (position < 0 || position >= itemCount) return
        items[position].turnUpdate(next)
        tryNotifyItemChanged(position)
    }

    fun clear() {
        val count = itemCount
        items.clear()
        tryNotifyItemRangeRemoved(0, count)
    }
}
