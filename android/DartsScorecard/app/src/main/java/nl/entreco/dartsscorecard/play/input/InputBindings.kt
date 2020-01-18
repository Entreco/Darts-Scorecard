package nl.entreco.dartsscorecard.play.input

import androidx.databinding.BindingAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView
import nl.entreco.dartsscorecard.R
import nl.entreco.domain.play.listeners.events.BustEvent
import nl.entreco.domain.play.listeners.events.NoScoreEvent
import nl.entreco.domain.play.listeners.events.SpecialEvent
import nl.entreco.domain.play.listeners.events.ThrownEvent

/**
 * Created by Entreco on 02/12/2017.
 */


object InputBindings {

    private const val defaultAnimationDuration: Long = 150
    private const val horizontalTranslation: Float = 0F
    private const val clearTranslation: Float = 10000F
    private const val delay: Long = 500

    @JvmStatic
    @BindingAdapter("scoreInput")
    fun showScore(view: TextView, scoreInput: String) {
        view.text = scoreInput
    }

    @JvmStatic
    @BindingAdapter("backAnimation")
    fun backAnimation(view: View, scoreInput: String) {
        if (scoreInput.isEmpty()) {
            view.animate().scaleX(0f).scaleY(0f).setDuration(defaultAnimationDuration).start()
        } else {
            view.animate().scaleX(1f).scaleY(1f).setDuration(defaultAnimationDuration).start()
        }
    }


    @JvmStatic
    @BindingAdapter("numDarts")
    fun numDartsState(view: View, dartsLeft: Int) {
        when (dartsLeft) {
            3 -> selectThreeDarts(view)
            2 -> selectTwoDarts(view)
            1 -> selectOneDart(view)
        }
    }

    private fun selectOneDart(view: View) {
        view.isSelected = false
        view.isActivated = false
    }

    private fun selectTwoDarts(view: View) {
        view.isSelected = false
        view.isActivated = true
    }

    private fun selectThreeDarts(view: View) {
        view.isSelected = true
        view.isActivated = false
    }

    @JvmStatic
    @BindingAdapter("ask4finish")
    fun showAsk4Finish(view: View, shouldAsk: Boolean) {
        view.visibility = if (shouldAsk) View.VISIBLE else View.GONE
        view.pivotY = view.height.toFloat()
        view.animate().setInterpolator(AccelerateDecelerateInterpolator()).setDuration(defaultAnimationDuration).scaleY(if (shouldAsk) 1F else horizontalTranslation).start()
    }


    @JvmStatic
    @BindingAdapter("special")
    fun showSpecialEvents(view: TextView, event: SpecialEvent?) {
        clear(view, 0)
        if (event != null) {
            when (event) {
                is NoScoreEvent -> handleNoScore(view)
                is ThrownEvent -> handleThrown(view, event.describe)
                is BustEvent -> handleBust(view)
            }
        }
    }

    private fun clear(view: TextView, delay: Long) {
        view.animate().translationX(-clearTranslation).setStartDelay(delay).withEndAction {
            view.text = ""
        }.setDuration(defaultAnimationDuration).start()
    }

    private fun handleThrown(view: TextView, describe: String) {
        view.text = describe
        view.animate().translationX(horizontalTranslation).setDuration(defaultAnimationDuration).withEndAction {
            clear(view, delay)
        }.start()
    }

    private fun handleNoScore(view: TextView) {
        view.setText(R.string.no_score)
        view.animate().translationX(horizontalTranslation).setDuration(defaultAnimationDuration).withEndAction {
            clear(view, delay)
        }.start()
    }

    private fun handleBust(view: TextView) {
        view.setText(R.string.bust)
        view.animate().translationX(horizontalTranslation).setDuration(defaultAnimationDuration).withEndAction {
            clear(view, delay)
        }.start()
    }
}