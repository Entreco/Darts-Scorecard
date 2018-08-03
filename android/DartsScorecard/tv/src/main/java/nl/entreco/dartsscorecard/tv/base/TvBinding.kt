package nl.entreco.dartsscorecard.tv.base

import android.databinding.BindingAdapter
import android.view.View

object TvBinding {

    @JvmStatic
    @BindingAdapter("showIf")
    fun doShowViewIf(view: View, show: Boolean){
        view.visibility = if(show) View.VISIBLE else View.GONE
    }
}