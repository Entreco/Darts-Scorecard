package nl.entreco.libads.ui

import nl.entreco.libads.Interstitials
import javax.inject.Inject
import javax.inject.Named

internal class InterstitialLoader @Inject constructor(
        @Named("interstitialId") private val interstitialId: String,
        private val interstitial: Interstitials) {

    init {
        interstitial.init(interstitialId)
    }

    fun showInterstitial() {
        interstitial.show()
    }
}