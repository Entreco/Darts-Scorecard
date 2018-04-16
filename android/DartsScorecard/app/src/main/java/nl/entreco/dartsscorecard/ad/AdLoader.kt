package nl.entreco.dartsscorecard.ad

import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Entreco on 29/12/2017.
 */
class AdLoader @Inject constructor(@Named("adId") private val adId: String) : AdListener() {

    interface AdListener {
        fun onAdLoaded()
    }

    private val adRequest by lazy { AdRequest.Builder().build() }
    private var listener: AdListener? = null

    override fun onAdLoaded() {
        super.onAdLoaded()
        listener?.onAdLoaded()
    }

    override fun onAdFailedToLoad(p0: Int) {
        super.onAdFailedToLoad(p0)
        listener?.onAdLoaded()
    }

    fun loadAd(view: AdView, adListener: AdListener) {
        listener = adListener
//        view.adSize = AdSize.SMART_BANNER
//        view.adUnitId = adId
        view.adListener = this
        view.loadAd(adRequest)
    }
}