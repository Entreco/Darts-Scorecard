package nl.entreco.dartsscorecard.ad

import android.databinding.BindingAdapter
import android.view.View
import com.google.android.gms.ads.AdView

/**
 * Created by Entreco on 29/12/2017.
 */
object AdBindings {
    @JvmStatic
    @BindingAdapter("viewModel")
    fun loadAd(view: AdView, viewModel: AdViewModel?) {
        viewModel?.provideAdd(view)
    }

    @JvmStatic
    @BindingAdapter("showAd")
    fun showAd(view: View, show: Boolean) {
        view.visibility = if (show) View.VISIBLE else View.GONE
    }
}