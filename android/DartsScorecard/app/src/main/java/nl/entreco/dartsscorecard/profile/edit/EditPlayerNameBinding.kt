package nl.entreco.dartsscorecard.profile.edit

import android.content.Context
import androidx.databinding.BindingAdapter
import androidx.annotation.StringRes
import com.google.android.material.textfield.TextInputLayout
import android.transition.TransitionInflater
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import nl.entreco.dartsscorecard.base.RevealAnimator

/**
 * Created by entreco on 02/03/2018.
 */

object EditPlayerNameBinding {
    @JvmStatic
    @BindingAdapter("showKeyboard", "window")
    fun showKeyboard(view: EditText, show: Boolean, window: Window) {
        val revealAnimator = RevealAnimator(view)
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (show) {
            revealAnimator.setupEnterAnimation(TransitionInflater.from(view.context), window)
            view.selectAll()
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        } else {
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    @JvmStatic
    @BindingAdapter("error")
    fun showError(view: TextInputLayout, @StringRes msg: Int) {
        if (msg != 0) {
            view.error = view.context.getString(msg)
        } else {
            view.error = ""
        }
    }
}
