package nl.entreco.dartsscorecard.profile.edit

import android.content.Context
import android.databinding.BindingAdapter
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

/**
 * Created by entreco on 02/03/2018.
 */
class EditPlayerNameBinding {

    companion object {
        @JvmStatic
        @BindingAdapter("showKeyboard")
        fun showKeyboard(view: EditText, show: Boolean) {
            val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (show) {
                view.selectAll()
                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
            } else {
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
    }
}
