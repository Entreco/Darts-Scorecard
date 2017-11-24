package nl.entreco.dartsscorecard.play.score

import android.content.Context
import android.databinding.BindingAdapter
import android.graphics.Color
import android.support.annotation.ColorRes
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView
import nl.entreco.dartsscorecard.CounterTextView
import nl.entreco.dartsscorecard.R
import nl.entreco.domain.play.model.players.Team
import nl.entreco.domain.settings.ScoreSettings
import android.R.attr.data
import android.support.annotation.ColorInt
import android.content.res.Resources.Theme
import android.util.TypedValue




/**
 * Created by Entreco on 20/11/2017.
 */
class ScoreBindings {
    companion object {
        @JvmStatic
        @BindingAdapter("adapter", "teams", "scoreSettings", "finishCalculator", requireAll = true)
        fun addTeams(recyclerView: RecyclerView, adapter: TeamScoreAdapter, teams: Array<Team>, scoreSettings: ScoreSettings, finishCalculator : FinishCalculator) {
            recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
            recyclerView.itemAnimator = null
            recyclerView.adapter = adapter
            adapter.clear()

            for (team in teams) {
                adapter.addItem(TeamScoreViewModel(team, scoreSettings.score(), finishCalculator))
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
            if (score <= 0) {
                view.animate().translationX(200F).setInterpolator(AccelerateDecelerateInterpolator()).setDuration(150).start()
            } else {
                view.animate().translationX(0f).setInterpolator(AccelerateDecelerateInterpolator()).setDuration(150).start()
                if (score == 180) {
                    view.setTextColor(view.context.getColor(R.color.colorOneEighty))
                } else {
                    view.setTextColor(fromAttr(view.context, R.attr.scoreText))
                }
            }
        }

        @ColorInt
        private fun fromAttr(context: Context, attr: Int): Int {
            val typedValue = TypedValue()
            val theme = context.theme
            theme.resolveAttribute(attr, typedValue, true)
            val res = typedValue.resourceId
            return context.getColor(res)
        }

        @JvmStatic
        @BindingAdapter("finish")
        fun showFinish(view: TextView, finish: String) {
            view.text = finish
            if (finish.isEmpty()) {
                view.animate().translationX(-200F).setInterpolator(AccelerateDecelerateInterpolator()).setDuration(150).start()
            } else {
                view.animate().translationX(0f).setInterpolator(AccelerateDecelerateInterpolator()).setDuration(150).start()
            }
        }
    }
}