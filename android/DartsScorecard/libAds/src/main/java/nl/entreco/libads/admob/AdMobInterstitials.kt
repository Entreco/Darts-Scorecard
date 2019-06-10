package nl.entreco.libads.admob

import android.content.Context
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import nl.entreco.libads.Interstitials

internal class AdMobInterstitials(context: Context) : Interstitials {
    private val adRequest: AdRequest by lazy { AdRequest.Builder().build() }
    private val interstitial = InterstitialAd(context)

    override fun init(interstitialId: String) {
        interstitial.adUnitId = interstitialId
        interstitial.adListener = object : AdListener() {
            override fun onAdClosed() {
                super.onAdClosed()
                if (interstitial.isLoaded) {
                    interstitial.show()
                }
            }
        }
        load()
    }

    override fun show() {
        load()
    }

    private fun load() = interstitial.loadAd(adRequest)
}