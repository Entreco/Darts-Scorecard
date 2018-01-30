package nl.entreco.dartsscorecard.play.input

import android.databinding.BindingAdapter
import android.support.design.widget.FloatingActionButton
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
abstract class InputBindings {


    companion object {
        private val DEFAULT_ANIMATION_TIME: Long = 150
        @JvmStatic
        @BindingAdapter("scoreInput")
        fun showScore(view: TextView, scoreInput: String) {
            view.text = scoreInput
        }

        @JvmStatic
        @BindingAdapter("backAnimation")
        fun backAnimation(view: View, scoreInput: String) {
            if (scoreInput.isEmpty()) {
                view.animate().scaleX(0f).scaleY(0f).setDuration(DEFAULT_ANIMATION_TIME).start()
            } else {
                view.animate().scaleX(1f).scaleY(1f).setDuration(DEFAULT_ANIMATION_TIME).start()
            }
        }


        @JvmStatic
        @BindingAdapter("numDarts")
        fun numDartsState(view: FloatingActionButton, dartsLeft: Int) {
            when (dartsLeft) {
                3 -> {
                    view.isSelected = true
                    view.isActivated = false
                }
                2 -> {
                    view.isSelected = false
                    view.isActivated = true
                }
                1 -> {
                    view.isSelected = false
                    view.isActivated = false
                }
            }
        }


        @JvmStatic
        @BindingAdapter("ask4finish")
        fun showAsk4Finish(view: View, shouldAsk: Boolean) {
            view.visibility = if (shouldAsk) View.VISIBLE else View.GONE
            view.pivotY = view.height.toFloat()
            view.animate().setInterpolator(AccelerateDecelerateInterpolator()).setDuration(DEFAULT_ANIMATION_TIME).scaleY(if (shouldAsk) 1F else 0F).start()
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
            view.animate().translationX(-2000F).setStartDelay(delay).withEndAction({
                view.text = ""
            }).setDuration(DEFAULT_ANIMATION_TIME).start()
        }

        private fun handleThrown(view: TextView, describe: String) {
            view.text = describe
            view.animate().translationX(0F).setDuration(DEFAULT_ANIMATION_TIME).withEndAction({
                clear(view, 500)
            }).start()
        }

        private fun handleNoScore(view: TextView) {
            view.setText(R.string.no_score)
            view.animate().translationX(0F).setDuration(DEFAULT_ANIMATION_TIME).withEndAction({
                clear(view, 500)
            }).start()
        }

        private fun handleBust(view: TextView) {
            view.setText(R.string.bust)
            view.animate().translationX(0F).setDuration(DEFAULT_ANIMATION_TIME).withEndAction({
                clear(view, 500)
            }).start()
        }
    }
}