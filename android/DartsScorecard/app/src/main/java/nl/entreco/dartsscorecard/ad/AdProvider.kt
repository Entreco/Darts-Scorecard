package nl.entreco.dartsscorecard.ad

import android.databinding.ObservableBoolean
import com.google.android.gms.ads.AdView
import javax.inject.Inject


class AdProvider @Inject constructor(
        private val adLoader: AdLoader,
        private val interstitialLoader: InterstitialLoader) {

    val showAd = ObservableBoolean(false)

    fun provideAdd(view: AdView) {
        adLoader.loadAd(view, object : AdLoader.AdListener {
            override fun onAdLoaded() {
                showAd.set(true)
            }
        })
    }

    fun provideInterstitial() {
        interstitialLoader.showInterstitial()
    }
}