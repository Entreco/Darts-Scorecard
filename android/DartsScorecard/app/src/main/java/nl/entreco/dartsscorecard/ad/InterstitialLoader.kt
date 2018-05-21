package nl.entreco.dartsscorecard.ad

import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import javax.inject.Inject
import javax.inject.Named


class InterstitialLoader @Inject constructor(
        @Named("interstitialId") private val interstitialId: String,
        private val interstitial: InterstitialAd) : AdListener() {

    private val adRequest: AdRequest by lazy { AdRequest.Builder().build() }

    init {
        interstitial.adUnitId = interstitialId
        interstitial.adListener = this
        interstitial.loadAd(adRequest)
    }

    fun showInterstitial() {
        if (interstitial.isLoaded) {
            interstitial.show()
        }
    }

    override fun onAdClosed() {
        super.onAdClosed()
        interstitial.loadAd(adRequest)
    }
}