package nl.entreco.dartsscorecard.play.score

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.databinding.TeamScoreViewBinding
import nl.entreco.domain.play.model.Next
import nl.entreco.domain.play.model.Score
import nl.entreco.domain.play.model.Turn
import nl.entreco.domain.play.model.players.Player
import javax.inject.Inject

/**
 * Created by Entreco on 22/11/2017.
 */
open class ScoreAdapter @Inject constructor(private val onReady: ReadyListener) : RecyclerView.Adapter<TeamScoreView>() {

    private val items = mutableListOf<TeamScoreViewModel>()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): TeamScoreView {
        val inflater = LazyInflater(parent?.context!!).inflater
        val binding = DataBindingUtil.inflate<TeamScoreViewBinding>(inflater, R.layout.team_score_view, parent, false)
        return TeamScoreView(binding)
    }

    override fun onBindViewHolder(holder: TeamScoreView?, position: Int) {
        holder?.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    open fun addItem(teamScoreViewModel: TeamScoreViewModel) {
        items.add(teamScoreViewModel)
        notifyItemInserted(items.size - 1)
    }

    open fun teamAtIndexScored(position: Int, score: Score, by: Player) {
        items[position].scored(score, by)
        notifyItemChanged(position)
    }

    open fun teamAtIndexThrew(position: Int, turn: Turn, player: Player) {
        items[position].threw(turn, player)
        notifyItemChanged(position)
    }

    open fun clear() {
        val count = itemCount
        items.clear()
        notifyItemRangeRemoved(0, count)
    }

    open fun teamAtIndexTurnUpdate(position: Int, next: Next) {
        if (position < 0 || position >= itemCount) return
        items[position].turnUpdate(next)
        notifyItemChanged(position)
    }

    open fun onTeamsReady() {
        onReady.onReady()
    }

    private class LazyInflater(context: Context) {
        val inflater: LayoutInflater by lazy { LayoutInflater.from(context) }
    }
}