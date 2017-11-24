package nl.entreco.dartsscorecard.play.score

import android.content.Context
import android.databinding.BindingAdapter
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView
import nl.entreco.dartsscorecard.CounterTextView
import nl.entreco.dartsscorecard.R
import nl.entreco.domain.play.model.players.Team
import nl.entreco.domain.settings.ScoreSettings
import android.R.attr.data
import android.support.v4.content.ContextCompat
import android.content.res.TypedArray
import android.content.res.Resources.Theme
import android.graphics.Color


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
        fun showCurrent(view: TextView, score: Int) {
            view.text = score.toString()
            if(score <= 0) {
                view.animate().translationX(200F).setInterpolator(AccelerateDecelerateInterpolator()).setDuration(150).start()
            } else {
                view.animate().translationX(0f).setInterpolator(AccelerateDecelerateInterpolator()).setDuration(150).start()
                if(score == 180){
                    view.setTextColor(Color.parseColor("#FCC804"))
                } else {
                    view.setTextColor(view.context.getColor(R.color.white))
                }
            }
        }

        @JvmStatic
        @BindingAdapter("finish")
        fun showFinish(view: TextView, finish: String) {
            view.text = finish
            if(finish.isEmpty()) {
                view.animate().translationX(-200F).setInterpolator(AccelerateDecelerateInterpolator()).setDuration(150).start()
            } else {
                view.animate().translationX(0f).setInterpolator(AccelerateDecelerateInterpolator()).setDuration(150).start()
            }
        }
    }
}