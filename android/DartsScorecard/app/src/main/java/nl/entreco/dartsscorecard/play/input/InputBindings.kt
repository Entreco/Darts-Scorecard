package nl.entreco.dartsscorecard.play.input

import android.databinding.BindingAdapter
import nl.entreco.dartsscorecard.base.widget.CounterTextView

/**
 * Created by Entreco on 02/12/2017.
 */
class InputBindings {

    companion object {
        @JvmStatic
        @BindingAdapter("scoreInput")
        fun showScore(view: CounterTextView, scoreInput: String) {
            try {
                val score = scoreInput.toInt()
                view.setTarget(score.toLong())
            } catch (e : NumberFormatException){
                view.text = scoreInput
            }
        }
    }
}