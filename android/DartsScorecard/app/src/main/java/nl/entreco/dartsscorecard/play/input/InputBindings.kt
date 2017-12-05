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
    }
}