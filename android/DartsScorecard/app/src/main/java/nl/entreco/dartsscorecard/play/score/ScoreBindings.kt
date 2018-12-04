package nl.entreco.dartsscorecard.play.score

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import nl.entreco.domain.model.Score
import nl.entreco.domain.model.players.Team
import nl.entreco.domain.play.finish.GetFinishUsecase
import nl.entreco.domain.settings.ScoreSettings


/**
 * Created by Entreco on 20/11/2017.
 */
object ScoreBindings {
    @JvmStatic
    @BindingAdapter("adapter", "teams", "scores", "scoreSettings", "finishUsecase", "uiCallback", requireAll = true)
    fun addTeams(recyclerView: androidx.recyclerview.widget.RecyclerView, adapter: ScoreAdapter, teams: ArrayList<Team>, scores: ArrayList<Score>, scoreSettings: ScoreSettings,
                 finishUsecase: GetFinishUsecase, uiCallback: UiCallback?) {

        if (teams.size != scores.size) {
            throw IllegalStateException("state mismatch, scores.size != teams.size! -> was this game already started?")
        }

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
                recyclerView.context)
        recyclerView.itemAnimator = null
        recyclerView.adapter = adapter
        adapter.clear()

        val listeners = mutableListOf<TeamScoreListener>()
        teams.forEachIndexed { index, team ->
            val vm = TeamScoreViewModel(team, scores[index], finishUsecase, starter = scoreSettings.teamStartIndex == index)
            adapter.addItem(vm)
            listeners.add(vm)
        }

        uiCallback?.onLetsPlayDarts(listeners)
    }

    @JvmStatic
    @BindingAdapter("currentTeam")
    fun scrollToCurrentTeam(recyclerView: androidx.recyclerview.widget.RecyclerView, currentTeamIndex: Int) {
        recyclerView.smoothScrollToPosition(currentTeamIndex)
    }

    @JvmStatic
    @BindingAdapter("description")
    fun setMatchDescription(view: TextView, desc: String?) {
        if(desc?.isNotBlank() == true)  {
            view.text = desc
        }
    }
}
