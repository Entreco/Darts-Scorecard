package nl.entreco.dartsscorecard.play.score

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.databinding.TeamScoreViewBinding
import nl.entreco.domain.play.model.Score
import javax.inject.Inject

/**
 * Created by Entreco on 22/11/2017.
 */
class TeamScoreAdapter @Inject constructor() : RecyclerView.Adapter<TeamScoreView>() {

    private val items = mutableListOf<TeamScoreViewModel>()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): TeamScoreView {
        val inflater = LayoutInflater.from(parent?.context)
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
        notifyItemInserted(items.size - 1)
    }

    fun teamAtIndexScored(position: Int, score: Score) {
        items[position].scored(score)
        notifyItemChanged(position)
    }

    fun teamAtIndexThrew(position: Int, dart: Int) {
        items[position].threw(dart)
        notifyItemChanged(position)
    }

    fun clear() {
        val count = itemCount
        items.clear()
        notifyItemRangeRemoved(0, count)
    }
}