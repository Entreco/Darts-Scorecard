package nl.entreco.dartsscorecard.play.score

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.databinding.TeamScoreViewBinding
import nl.entreco.domain.play.listeners.PlayerListener
import nl.entreco.domain.play.listeners.SpecialEventListener
import nl.entreco.domain.play.listeners.events.SpecialEvent
import nl.entreco.domain.play.model.Next
import nl.entreco.domain.play.model.Score
import nl.entreco.domain.play.model.Turn
import nl.entreco.domain.play.model.players.Player
import javax.inject.Inject

/**
 * Created by Entreco on 22/11/2017.
 */
class ScoreAdapter @Inject constructor() : RecyclerView.Adapter<TeamScoreView>() {

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

    private class LazyInflater(context: Context) {
        val inflater: LayoutInflater by lazy { LayoutInflater.from(context) }
    }

    private fun tryNotifyItemInserted(position: Int) {
        try { notifyItemInserted(position) } catch (ignore : NullPointerException){}
    }
    private fun tryNotifyItemChanged(position: Int) {
        try { notifyItemChanged(position) } catch (ignore : NullPointerException){}
    }

    private fun tryNotifyItemRangeRemoved(position : Int, count: Int){
        try {  notifyItemRangeRemoved(position, count) } catch (ignore : NullPointerException){}
    }
}