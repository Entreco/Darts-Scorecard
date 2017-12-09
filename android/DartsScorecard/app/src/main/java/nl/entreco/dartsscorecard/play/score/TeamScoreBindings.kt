package nl.entreco.dartsscorecard.play.score

import android.animation.ValueAnimator
import android.content.Context
import android.databinding.BindingAdapter
import android.graphics.Color
import android.support.annotation.ColorInt
import android.support.v4.graphics.ColorUtils
import android.util.TypedValue
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.base.widget.CounterTextView
import nl.entreco.dartsscorecard.play.input.InputBindings
import nl.entreco.domain.play.listeners.events.NoScoreEvent
import nl.entreco.domain.play.listeners.events.OneEightyEvent

/**
 * Created by Entreco on 25/11/2017.
 */
class TeamScoreBindings {
    companion object {

        private val DEFAULT_ANIMATION_TIME: Long = 150

        @JvmStatic
        @BindingAdapter("score")
        fun showScore(view: CounterTextView, score: Int) {
            view.setTarget(score.toLong())
        }

        @JvmStatic
        @BindingAdapter("currentPlayer")
        fun showCurrentScore(view: CounterTextView, score: Int) {
            view.setTarget(score.toLong())
            if (score <= 0) {
                view.animate().translationX(200F).setInterpolator(AccelerateDecelerateInterpolator()).setDuration(DEFAULT_ANIMATION_TIME).start()
            } else {
                view.animate().translationX(0f).setInterpolator(AccelerateDecelerateInterpolator()).setDuration(DEFAULT_ANIMATION_TIME).start()
                if (score == 180) {
                    view.setTextColor(view.context.getColor(R.color.colorOneEighty))
                } else {
                    view.setTextColor(fromAttr(view.context, R.attr.scoreText))
                }
            }
        }

        @JvmStatic
        @BindingAdapter("currentTeam")
        fun showCurrentTeam(view: ImageView, isCurrentTeam: Boolean) {
            val w = if (view.width.toFloat() <= 0.0f) 200f else view.width.toFloat()
            view.animate().translationX(if (isCurrentTeam) 0f else w).setInterpolator(AccelerateDecelerateInterpolator()).setDuration(DEFAULT_ANIMATION_TIME).start()
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
        @BindingAdapter("finish", "finishAlpha", requireAll = false)
        fun showFinishWithAlpha(view: TextView, finish: String, isCurrentTeam: Boolean) {
            view.text = finish
            if (finish.isEmpty()) {
                view.animate()
                        .translationX(-200F)
                        .setInterpolator(AccelerateDecelerateInterpolator())
                        .setDuration(DEFAULT_ANIMATION_TIME).start()
            } else {
                view.animate()
                        .translationX(0f)
                        .setInterpolator(AccelerateDecelerateInterpolator())
                        .setDuration(DEFAULT_ANIMATION_TIME).start()

                val startColor = colorToHex(view.currentTextColor)
                val endColor = if (isCurrentTeam) "#FFFFFFFF" else "#aaFFFFFF"
                if (startColor != endColor) {
                    val valueAnimator = ValueAnimator.ofFloat(0.0f, 1.0f).setDuration(DEFAULT_ANIMATION_TIME)
                    valueAnimator.addUpdateListener {
                        val fractionAnim = it.animatedValue as Float
                        view.setTextColor(ColorUtils.blendARGB(Color.parseColor(startColor), Color.parseColor(endColor), fractionAnim))
                    }
                    valueAnimator.start()
                }
            }
        }

        private fun colorToHex(color: Int): String {
            val alpha = android.graphics.Color.alpha(color)
            val blue = android.graphics.Color.blue(color)
            val green = android.graphics.Color.green(color)
            val red = android.graphics.Color.red(color)

            val alphaHex = to00Hex(alpha)
            val blueHex = to00Hex(blue)
            val greenHex = to00Hex(green)
            val redHex = to00Hex(red)

            // hexBinary value: aabbggrr
            val str = StringBuilder("#")
            str.append(alphaHex)
            str.append(blueHex)
            str.append(greenHex)
            str.append(redHex)

            return str.toString()
        }

        private fun to00Hex(value: Int): String {
            val hex = "00" + Integer.toHexString(value)
            return hex.substring(hex.length - 2, hex.length)
        }

        @JvmStatic
        @BindingAdapter("started")
        fun showStartingPlayer(view: TextView, started: Boolean) {
            if (started) {
                view.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.starter, 0)
            } else {
                view.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            }
        }
    }
}