package nl.entreco.dartsscorecard.play.score

import android.databinding.BindingAdapter
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import nl.entreco.domain.play.model.players.Team
import nl.entreco.domain.play.usecase.GetFinishUsecase
import nl.entreco.domain.settings.ScoreSettings


/**
 * Created by Entreco on 20/11/2017.
 */
class ScoreBindings {
    companion object {
        @JvmStatic
        @BindingAdapter("adapter", "teams", "scoreSettings", "getFinishUsecase", requireAll = true)
        fun addTeams(recyclerView: RecyclerView, adapter: ScoreAdapter, teams: Array<Team>, scoreSettings: ScoreSettings, getFinishUsecase: GetFinishUsecase) {
            recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
            recyclerView.itemAnimator = null
            recyclerView.adapter = adapter
            adapter.clear()

            for (team in teams) {
                adapter.addItem(TeamScoreViewModel(team, scoreSettings.score(), getFinishUsecase))
            }
        }
    }
}