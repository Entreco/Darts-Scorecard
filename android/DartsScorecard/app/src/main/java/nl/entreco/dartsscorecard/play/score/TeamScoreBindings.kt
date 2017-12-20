package nl.entreco.dartsscorecard.play.score

import android.animation.ValueAnimator
import android.content.Context
import android.databinding.BindingAdapter
import android.graphics.Color
import android.support.annotation.ColorInt
import android.support.v4.graphics.ColorUtils
import android.util.Log
import android.util.TypedValue
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import android.widget.TextView
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.base.widget.CounterTextView

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
        @BindingAdapter("special")
        fun showSpecials(view: TextView, oldScore: Int, score: Int) {
            val diff = oldScore - score
            when (diff) {
                180 -> handle180(view)
                0 -> {}
                else -> { clear(view, 0)}
            }
        }

        private fun clear(view: TextView, delay: Long) {
            view.animate().cancel()
            view.animate().translationX(200F).setStartDelay(delay).withEndAction({
                view.setText( R.string.empty )
            }).setDuration(DEFAULT_ANIMATION_TIME).start()
        }

        private fun handle180(view: TextView) {
            view.animate().cancel()
            view.setText( R.string.score_180 )
            view.animate().translationX(0F).setInterpolator(OvershootInterpolator()).setDuration(DEFAULT_ANIMATION_TIME)
                    .withEndAction({
                        val howLong = 1200L
                        animateColor(view, R.attr.colorOneEighty, R.attr.scoreText, howLong)
                        clear(view, howLong)
                    }).start()
        }

        private fun animateColor(view: TextView, attr: Int, attr2: Int, duration: Long) {
            view.animate().setDuration(duration / 3)
                    .setStartDelay(duration / 3)
                    .withStartAction{ view.setTextColor(fromAttr(view.context, attr)) }
                    .withEndAction { view.setTextColor(fromAttr(view.context, attr2)) }
                    .start()
        }


        @JvmStatic
        @BindingAdapter("currentScore")
        fun showCurrentScore(view: CounterTextView, score: Int) {
            view.setTarget(score.toLong())
            if (score <= 0) {
                view.animate().translationX(200F).setInterpolator(AccelerateDecelerateInterpolator()).setDuration(DEFAULT_ANIMATION_TIME).start()
            } else {
                view.animate().translationX(0f).setInterpolator(AccelerateDecelerateInterpolator()).setDuration(DEFAULT_ANIMATION_TIME).start()
                view.setTextColor(fromAttr(view.context, R.attr.scoreText))
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
                Log.e("REMCO", "COLORINT:${view.currentTextColor}")
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
            val alpha = Color.alpha(color)
            val blue = Color.blue(color)
            val green = Color.green(color)
            val red = Color.red(color)

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