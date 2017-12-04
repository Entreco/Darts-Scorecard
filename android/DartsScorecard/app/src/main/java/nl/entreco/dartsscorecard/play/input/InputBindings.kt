package nl.entreco.dartsscorecard.play.input

import android.databinding.BindingAdapter
import android.support.design.widget.FloatingActionButton
import android.util.Log
import android.view.View
import android.widget.TextView
import nl.entreco.domain.play.model.Turn

/**
 * Created by Entreco on 02/12/2017.
 */
class InputBindings {


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
        @BindingAdapter("fabAnimation")
        fun fabAnimation(view: FloatingActionButton, turn: Turn?) {
            if (turn != null) {

                val dartsLeft = turn.dartsLeft()
                Log.d("WOW", "dartsLeft:$dartsLeft")
                when (dartsLeft) {
                    3 -> {
                        view.isActivated = true
                        view.isSelected = false
                        view.isEnabled = true
                    }
                    2 -> {
                        view.isActivated = false
                        view.isSelected = true
                        view.isEnabled = true
                    }
                    1 -> {
                        view.isActivated = false
                        view.isSelected = false
                        view.isEnabled = true
                    }
                    else -> {
                        view.isSelected = false
                        view.isActivated = false
                        view.isEnabled = false
                    }
                }
            } else {
                Log.d("WOW", "dartsLeft:null -> reset")
                view.isActivated = true
                view.isSelected = true
                view.isEnabled = true
            }
        }
    }
}