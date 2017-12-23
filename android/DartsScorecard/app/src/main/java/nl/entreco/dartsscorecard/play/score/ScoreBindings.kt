package nl.entreco.dartsscorecard.play.score

import android.databinding.BindingAdapter
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import nl.entreco.domain.model.Score
import nl.entreco.domain.model.players.Team
import nl.entreco.domain.play.usecase.GetFinishUsecase
import nl.entreco.domain.settings.ScoreSettings


/**
 * Created by Entreco on 20/11/2017.
 */
class ScoreBindings {
    companion object {
        @JvmStatic
        @BindingAdapter("adapter", "teams", "scores", "scoreSettings", "finishUsecase", "uiCallback", requireAll = true)
        fun addTeams(recyclerView: RecyclerView, adapter: ScoreAdapter, teams: ArrayList<Team>, scores: ArrayList<Score>, scoreSettings: ScoreSettings, finishUsecase: GetFinishUsecase, uiCallback: UiCallback?) {

            recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
            recyclerView.itemAnimator = null
            recyclerView.adapter = adapter
            adapter.clear()

            teams.forEachIndexed { index, team ->
                val vm = TeamScoreViewModel(team, scores[index], finishUsecase, starter = scoreSettings.teamStartIndex == index)
                adapter.addItem(vm)
            }

            uiCallback?.onLetsPlayDarts()
        }
    }
}