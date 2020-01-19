package nl.entreco.libads.ui

import android.os.Bundle
import android.view.View
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import nl.entreco.libconsent.fetch.FetchConsentUsecase
import javax.inject.Inject

/**
 * Created by Entreco on 29/12/2017.
 */
internal class AdLoader @Inject constructor(
        private val fetch: FetchConsentUsecase
) : AdListener() {

    interface AdListener {
        fun onAdLoaded()
        fun onAdFailed()
    }

    private val adRequestBuilder by lazy { AdRequest.Builder() }
    private var listener: AdListener? = null

    override fun onAdLoaded() {
        super.onAdLoaded()
        listener?.onAdLoaded()
    }

    override fun onAdFailedToLoad(p0: Int) {
        super.onAdFailedToLoad(p0)
        listener?.onAdFailed()
    }

    fun loadAd(view: View, adListener: AdListener) {
        listener = adListener
        (view as? AdView)?.let { v ->
            v.adListener = this
            fetch.go {
                val request = buildRequest(it.nonPersonalized)
                v.loadAd(request)
            }
        }
    }

    private fun buildRequest(it: Boolean): AdRequest? {
        return if (it) {
            val bundle = Bundle().apply { putString("npa", "1") }
            adRequestBuilder.addNetworkExtrasBundle(AdMobAdapter::class.java, bundle).build()
        } else adRequestBuilder.build()
    }
}