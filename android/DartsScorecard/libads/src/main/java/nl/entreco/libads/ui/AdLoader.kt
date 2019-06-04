package nl.entreco.libads.ui

import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import javax.inject.Inject

/**
 * Created by Entreco on 29/12/2017.
 */
class AdLoader @Inject constructor() : AdListener() {

    interface AdListener {
        fun onAdLoaded()
        fun onAdFailed()
    }

    private val adRequest by lazy { AdRequest.Builder().build() }
    private var listener: AdListener? = null

    override fun onAdLoaded() {
        super.onAdLoaded()
        listener?.onAdLoaded()
    }

    override fun onAdFailedToLoad(p0: Int) {
        super.onAdFailedToLoad(p0)
        listener?.onAdFailed()
    }

    fun loadAd(view: AdView, adListener: AdListener) {
        listener = adListener
        view.adListener = this
        view.loadAd(adRequest)
    }
}