package nl.entreco.dartsscorecard.setup.ad

import com.google.android.gms.ads.AdListener
import javax.inject.Inject

/**
 * Created by Entreco on 29/12/2017.
 */
class AdLoader @Inject constructor(private val listener: Listener) : AdListener() {
    interface Listener {
        fun onAdClicked()
        fun onAdLoaded()
        fun onAdFailed(err: Int)
    }

    override fun onAdClicked() {
        super.onAdClicked()
        listener.onAdClicked()
    }

    override fun onAdLoaded() {
        super.onAdLoaded()
        listener.onAdLoaded()
    }

    override fun onAdFailedToLoad(err: Int) {
        super.onAdFailedToLoad(err)
        listener.onAdFailed(err)
    }
}