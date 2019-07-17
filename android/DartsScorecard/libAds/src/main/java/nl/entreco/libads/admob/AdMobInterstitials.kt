package nl.entreco.libads.admob

import android.content.Context
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import nl.entreco.libads.Interstitials
import javax.inject.Named

internal class AdMobInterstitials(
        context: Context,
        @Named("interstitialId") private val interstitialId: String
) : Interstitials {
    private val adRequest: AdRequest by lazy { AdRequest.Builder().build() }
    private val interstitial = InterstitialAd(context)

    init {
        interstitial.adUnitId = interstitialId
        interstitial.adListener = object : AdListener() {

            override fun onAdLoaded() {
                super.onAdLoaded()
                if (interstitial.isLoaded) {
                    interstitial.show()
                }
            }
        }
    }

    override fun show() {
        load()
    }

    private fun load() = interstitial.loadAd(adRequest)
}