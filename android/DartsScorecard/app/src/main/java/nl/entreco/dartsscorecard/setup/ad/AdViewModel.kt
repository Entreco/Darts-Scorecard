package nl.entreco.dartsscorecard.setup.ad

import android.databinding.ObservableBoolean
import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.domain.Analytics
import javax.inject.Inject

/**
 * Created by Entreco on 29/12/2017.
 */
class AdViewModel @Inject constructor(private val analytics: Analytics) : BaseViewModel(), AdLoader.Listener {

    val loadAd = ObservableBoolean(true)
    val showAd = ObservableBoolean(false)
    val adLoader = AdLoader(this)

    override fun onAdClicked() {
        analytics.trackAchievement("onAdClicked")
    }

    override fun onAdLoaded() {
        showAd.set(true)
    }

    override fun onAdFailed(err: Int) {
        showAd.set(false)
    }
}