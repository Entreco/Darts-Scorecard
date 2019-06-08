package nl.entreco.libads.ui

import android.os.Bundle
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import nl.entreco.libconsent.fetch.FetchConsentResponse
import nl.entreco.libconsent.fetch.FetchCurrentConsentUsecase
import javax.inject.Inject

/**
 * Created by Entreco on 29/12/2017.
 */
class AdLoader @Inject constructor(
        private val fetchCurrentConsentUsecase: FetchCurrentConsentUsecase
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

    fun loadAd(view: AdView, adListener: AdListener) {
        listener = adListener
        view.adListener = this

        fetchCurrentConsentUsecase.go {
            val request = buildRequest(it)
            view.loadAd(request)
        }
    }

    private fun buildRequest(it: FetchConsentResponse): AdRequest? {
        return if (it.nonPersonalized) {
            val bundle = Bundle().apply { putString("npa", "1") }
            adRequestBuilder.addNetworkExtrasBundle(AdMobAdapter::class.java, bundle).build()
        } else adRequestBuilder.build()
    }
}