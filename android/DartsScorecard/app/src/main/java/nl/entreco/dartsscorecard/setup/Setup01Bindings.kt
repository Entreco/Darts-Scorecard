package nl.entreco.dartsscorecard.setup

import android.databinding.BindingAdapter
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView

/**
 * Created by Entreco on 28/12/2017.
 */
class Setup01Bindings {
    companion object {
        @JvmStatic
        @BindingAdapter("loadAdd")
        fun loadAdd(view: AdView, load: Boolean) {
            if (load) {
                val adRequest = AdRequest.Builder()
                        .build()
                view.loadAd(adRequest)
            }
        }
    }
}