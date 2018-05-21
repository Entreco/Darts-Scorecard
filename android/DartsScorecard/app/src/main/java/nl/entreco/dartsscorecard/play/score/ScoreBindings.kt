package nl.entreco.dartsscorecard.play.score

import android.databinding.BindingAdapter
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import nl.entreco.domain.model.Score
import nl.entreco.domain.model.players.Team
import nl.entreco.domain.play.finish.GetFinishUsecase
import nl.entreco.domain.settings.ScoreSettings


/**
 * Created by Entreco on 20/11/2017.
 */
abstract class ScoreBindings {
    companion object {
        @JvmStatic
        @BindingAdapter("adapter", "teams", "scores", "scoreSettings", "finishUsecase", "uiCallback", requireAll = true)
        fun addTeams(recyclerView: RecyclerView, adapter: ScoreAdapter, teams: ArrayList<Team>, scores: ArrayList<Score>, scoreSettings: ScoreSettings,
                     finishUsecase: GetFinishUsecase, uiCallback: UiCallback?) {

            if (teams.size != scores.size) {
                throw IllegalStateException("state mismatch, scores.size != teams.size! -> was this game already started?")
            }

            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
            recyclerView.itemAnimator = null
            recyclerView.isDrawingCacheEnabled = true
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
        fun scrollToCurrentTeam(recyclerView: RecyclerView, currentTeamIndex: Int) {
            recyclerView.smoothScrollToPosition(currentTeamIndex)
        }
    }
}
