package nl.entreco.dartsscorecard.profile.edit

import android.content.Context
import android.databinding.BindingAdapter
import android.transition.TransitionInflater
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import nl.entreco.dartsscorecard.base.RevealAnimator

/**
 * Created by entreco on 02/03/2018.
 */
class EditPlayerNameBinding {

    companion object {
        @JvmStatic
        @BindingAdapter("showKeyboard", "window")
        fun showKeyboard(view: EditText, show: Boolean, window: Window) {
            val revealAnimator = RevealAnimator(view)
            val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (show) {
                revealAnimator.setupEnterAnimation(TransitionInflater.from(view.context), window, view.rootView)
                view.selectAll()
                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
            } else {
                revealAnimator.setupExitAnimation(TransitionInflater.from(view.context), window, view)
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
    }
}
