package nl.entreco.dartsscorecard.ad

import androidx.databinding.BindingAdapter
import android.os.Handler
import android.view.View
import com.google.android.gms.ads.AdView

/**
 * Created by Entreco on 29/12/2017.
 */
object AdBindings {
    @JvmStatic
    @BindingAdapter("viewModel", "adDelay", requireAll = false)
    fun loadAd(view: AdView, viewModel: AdViewModel?, adDelay: Int?) {
        val delay = adDelay?.toLong() ?: 0L
        Handler().postDelayed({
            viewModel?.provideAdd(view)
        }, delay)
    }

    @JvmStatic
    @BindingAdapter("showAd")
    fun showAd(view: View, show: Boolean) {
        view.visibility = if (show) View.VISIBLE else View.GONE
    }
}