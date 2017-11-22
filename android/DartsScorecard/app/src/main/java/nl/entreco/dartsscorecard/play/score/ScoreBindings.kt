package nl.entreco.dartsscorecard.play.score

import android.databinding.BindingAdapter
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.animation.AccelerateDecelerateInterpolator
import nl.entreco.dartsscorecard.CounterTextView
import nl.entreco.domain.play.model.players.Team
import nl.entreco.domain.settings.ScoreSettings

/**
 * Created by Entreco on 20/11/2017.
 */
class ScoreBindings {
    companion object {
        @JvmStatic
        @BindingAdapter("adapter", "teams", "scoreSettings", requireAll = true)
        fun addTeams(recyclerView: RecyclerView, adapter: TeamScoreAdapter, teams: Array<Team>, scoreSettings: ScoreSettings) {
            recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
            recyclerView.itemAnimator = null
            recyclerView.adapter = adapter
            adapter.clear()

            for (team in teams) {
                adapter.addItem(TeamScoreViewModel(team, scoreSettings.score()))
            }
        }

        @JvmStatic
        @BindingAdapter("score")
        fun showScore(view: CounterTextView, score: Int) {
            view.setTarget(score.toLong())
        }

        @JvmStatic
        @BindingAdapter("current")
        fun showCurrent(view: CounterTextView, score: Int) {
            view.setTarget(score.toLong())
            if(score <= 0) {
                view.animate().translationX(200f).setInterpolator(AccelerateDecelerateInterpolator()).setDuration(50).start()
            } else {
                view.animate().translationX(0f).setInterpolator(AccelerateDecelerateInterpolator()).setDuration(50).start()
            }
        }
    }
}