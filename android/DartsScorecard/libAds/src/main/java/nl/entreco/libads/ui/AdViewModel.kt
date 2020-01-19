package nl.entreco.libads.ui

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.ads.AdView
import nl.entreco.domain.ad.FetchPurchasedItemsResponse
import nl.entreco.domain.ad.FetchPurchasedItemsUsecase
import nl.entreco.domain.beta.donations.MakePurchaseResponse
import nl.entreco.libads.Ads
import nl.entreco.libads.BuildConfig
import nl.entreco.libads.Interstitials
import nl.entreco.libconsent.fetch.FetchConsentUsecase
import nl.entreco.shared.scopes.ActivityScope
import nl.entreco.shared.toSingleEvent
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import javax.inject.Named

@ActivityScope
class AdViewModel @Inject constructor(
        private val fetchPurchasedItemsUsecase: FetchPurchasedItemsUsecase,
        private val fetchConsentUsecase: FetchConsentUsecase,
        private val adLoader: Ads,
        private val interstitialLoader: Interstitials,
        @Named("debugMode") private val debug: Boolean = BuildConfig.DEBUG) : ViewModel(), LifecycleObserver {

    val showAd = ObservableBoolean(false)
    private val showConsent by lazy { MutableLiveData<Boolean>() }
    private var serveAds = AtomicBoolean(false) // Let's give the user no Ads by default

    fun consent(): LiveData<Boolean> = showConsent.toSingleEvent()

    fun provideAdd(view: AdView) {
        if (!debug) {
            adLoader.load(view) {
                showAd.set(serveAds.get())
            }
        }
    }

    fun provideInterstitial() {
        if (!debug && serveAds.get()) {
            interstitialLoader.show()
        }
    }

    fun onPurchasesRetrieved(response: MakePurchaseResponse.Updated) {
        fetchPurchasedItemsUsecase.exec(done(response), {})
    }

    private fun done(response: MakePurchaseResponse.Updated): (FetchPurchasedItemsResponse) -> Unit {
        return { adResponse ->
            fetchConsentUsecase.go { consentResponse ->
                val shouldServeAds = response.purchases.none { it.state == 1 } && adResponse.serveAds
                showConsent.postValue(consentResponse.shouldAskForConsent && shouldServeAds)
                serveAds.set(shouldServeAds)
            }
        }
    }
}