package nl.entreco.dartsscorecard.tv

import android.databinding.BindingAdapter
import android.view.View
import android.view.ViewGroup

object TvBinding {

    @JvmStatic
    @BindingAdapter("showIf")
    fun doShowViewIf(view: View, show: Boolean){
        view.visibility = if(show) View.VISIBLE else View.GONE
    }
}