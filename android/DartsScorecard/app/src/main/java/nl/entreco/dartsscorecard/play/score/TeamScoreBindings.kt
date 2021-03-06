package nl.entreco.dartsscorecard.play.score

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.graphics.ColorUtils
import androidx.core.view.doOnLayout
import androidx.databinding.BindingAdapter
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.base.widget.CounterTextView


/**
 * Created by Entreco on 25/11/2017.
 */
object TeamScoreBindings {

    private const val DEFAULT_ANIMATION_TIME: Long = 150

    @JvmStatic
    @BindingAdapter("score")
    fun showScore(view: CounterTextView, score: Int) {
        view.setTarget(score.toLong())
    }

    @JvmStatic
    @BindingAdapter("botText")
    fun showBotText(view: CounterTextView, score: String) {
        view.visibility = if(score.isNotBlank()) View.VISIBLE else View.INVISIBLE
        if(score.isNotBlank()) view.text = score
        else view.text = ""
    }

    @JvmStatic
    @BindingAdapter("nine")
    fun showNineDarter(view: TextView, possibleNineDart: Boolean) {
        if (possibleNineDart) handleNine(view)
        else clearNine(view)
    }

    private fun handleNine(view: TextView) {
        show(view)
    }

    private fun clearNine(view: TextView) {
        hide(view)
    }

    @JvmStatic
    @BindingAdapter("specialScore")
    fun showSpecials(view: TextView, oldScore: Int, score: Int) {
        when (oldScore - score) {
            180 -> handle180(view)
            0 -> { /* nothing */ }
            else -> clear(view)
        }
    }

    private fun clear(view: TextView, delay: Long = 0) {
        view.animate()
                .scaleY(0F)
                .setStartDelay(delay)
                .setInterpolator(DecelerateInterpolator())
                .withStartAction {
                    view.pivotY = view.height.toFloat()
                }
                .withEndAction {
                    view.scaleY = 0F
                    view.translationY = 0F
                    view.setText(R.string.empty)
                }.setDuration(if (delay == 0L) 0 else DEFAULT_ANIMATION_TIME).start()
    }

    private fun handle180(view: TextView) {
        view.animate().cancel()
        view.setText(R.string.score_180)
        view.animate()
                .scaleY(1F)
                .setStartDelay(0L)
                .setDuration(DEFAULT_ANIMATION_TIME)
                .setInterpolator(AccelerateInterpolator())
                .withStartAction {
                    view.scaleY = 0F
                    view.pivotY = 0F
                }
                .withEndAction {
                    animateColor(view, R.attr.colorOneEighty, R.attr.scoreText, 1200L)
                    clear(view, 1200L)
                }.start()
    }

    private fun animateColor(view: TextView, attr: Int, attr2: Int, duration: Long) {
        view.animate()
                .setDuration(duration / 3)
                .setStartDelay(duration / 3)
                .withStartAction { view.setTextColor(fromAttr(view.context, attr)) }
                .withEndAction { view.setTextColor(fromAttr(view.context, attr2)) }
                .start()
    }


    @JvmStatic
    @BindingAdapter("currentScore")
    fun showCurrentScore(view: CounterTextView, score: Int) {
        view.setTarget(score.toLong())
        if (score <= 0) {
            view.doOnLayout {
                view.animate().translationX(view.width.toFloat())
                        .setInterpolator(AccelerateDecelerateInterpolator())
                        .setDuration(DEFAULT_ANIMATION_TIME).start()
            }
        } else {
            view.animate().translationX(0f).setInterpolator(AccelerateDecelerateInterpolator())
                    .setDuration(DEFAULT_ANIMATION_TIME).start()
            view.setTextColor(fromAttr(view.context, R.attr.scoreText))
        }
    }

    @JvmStatic
    @BindingAdapter("currentTeam")
    fun showCurrentTeam(view: ImageView, isCurrentTeam: Boolean) {
        var target = 0F
        if (!isCurrentTeam) {
            view.measure(0, 0)
            val w = view.width.toFloat() * 3
            target = if (w == 0F) 50F else w
        }

        view.animate().translationX(target).setInterpolator(AccelerateDecelerateInterpolator())
                .setDuration(DEFAULT_ANIMATION_TIME).start()
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
        view.measure(0, 0)
        if (finish.isEmpty()) {
            hide(view)
        } else {
            show(view)

            val startColor = colorToHex(view.currentTextColor)
            val endColor = if (isCurrentTeam) "#FFFFFFFF" else "#aaFFFFFF"
            if (startColor != endColor) {
                val valueAnimator = ValueAnimator.ofFloat(0.0f, 1.0f)
                        .setDuration(DEFAULT_ANIMATION_TIME)
                valueAnimator.addUpdateListener {
                    val fractionAnim = it.animatedValue as Float
                    view.setTextColor(ColorUtils.blendARGB(Color.parseColor(startColor),
                            Color.parseColor(endColor), fractionAnim))
                }
                valueAnimator.start()
            }
        }
    }

    private fun hide(view: TextView) {
        view.measure(0, 0)
        val animator = ValueAnimator.ofInt(view.width, 0).setDuration(DEFAULT_ANIMATION_TIME)
        animator.addUpdateListener {
            val lp = view.layoutParams
            lp.width = it.animatedValue as Int
            view.layoutParams = lp
            view.parent.requestLayout()
        }
        animator.start()
    }

    private fun show(view: TextView) {
        view.measure(0, 0)
        val animator = ValueAnimator.ofInt(view.width, view.measuredWidth)
                .setDuration(DEFAULT_ANIMATION_TIME)
        animator.addUpdateListener {
            val lp = view.layoutParams
            lp.width = it.animatedValue as Int
            view.layoutParams = lp
            view.parent.requestLayout()
        }
        animator.start()
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
