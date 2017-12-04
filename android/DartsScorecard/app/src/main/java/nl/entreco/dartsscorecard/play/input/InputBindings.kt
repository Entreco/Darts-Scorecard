package nl.entreco.dartsscorecard.play.input

import android.databinding.BindingAdapter
import android.widget.TextView

/**
 * Created by Entreco on 02/12/2017.
 */
class InputBindings {

    companion object {
        @JvmStatic
        @BindingAdapter("scoreInput")
        fun showScore(view: TextView, scoreInput: String) {
            view.text = scoreInput
        }
    }
}