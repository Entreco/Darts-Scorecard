package nl.entreco.dartsscorecard.setup.ad

import android.databinding.BindingAdapter
import android.view.View
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView

/**
 * Created by Entreco on 29/12/2017.
 */
class AdBindings {
    companion object {
        @JvmStatic
        @BindingAdapter("loadAdd", "adListener")
        fun loadAd(view: AdView, load: Boolean, adListener: AdListener) {
            if (load) {
                val adRequest = AdRequest.Builder().build()
                view.adListener = adListener
                view.loadAd(adRequest)
            }
        }

        @JvmStatic
        @BindingAdapter("showAd")
        fun showAd(view: View, show: Boolean) {
            view.visibility = if (show) View.VISIBLE else View.GONE
        }
    }
}