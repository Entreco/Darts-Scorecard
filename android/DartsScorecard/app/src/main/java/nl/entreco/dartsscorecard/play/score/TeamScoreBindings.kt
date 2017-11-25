package nl.entreco.dartsscorecard.play.score

import android.content.Context
import android.databinding.BindingAdapter
import android.support.annotation.ColorInt
import android.util.TypedValue
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.base.widget.CounterTextView

/**
 * Created by Entreco on 25/11/2017.
 */
class TeamScoreBindings {
    companion object {

        @JvmStatic
        @BindingAdapter("score")
        fun showScore(view: CounterTextView, score: Int) {
            view.setTarget(score.toLong())
        }

        @JvmStatic
        @BindingAdapter("current")
        fun showCurrent(view: CounterTextView, score: Int) {
            view.setTarget(score.toLong())
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

        @JvmStatic
        @BindingAdapter("started")
        fun showStartingPlayer(view: TextView, started: Boolean) {
            if(started){
                view.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.starter, 0)
            } else {
                view.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            }
        }
    }
}