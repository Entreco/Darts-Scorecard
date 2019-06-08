package nl.entreco.libads.ui

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import com.google.android.gms.ads.AdView
import nl.entreco.domain.ad.FetchPurchasedItemsResponse
import nl.entreco.domain.ad.FetchPurchasedItemsUsecase
import nl.entreco.domain.purchases.connect.ConnectToBillingUsecase
import nl.entreco.libconsent.fetch.FetchCurrentConsentUsecase
import nl.entreco.shared.scopes.ActivityScope
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import javax.inject.Named

@ActivityScope
class AdViewModel @Inject constructor(
        @ActivityScope private val lifeCycle: Lifecycle,
        @ActivityScope private val connectToBillingUsecase: ConnectToBillingUsecase,
        private val fetchPurchasedItemsUsecase: FetchPurchasedItemsUsecase,
        private val fetch: FetchCurrentConsentUsecase,
        private val adLoader: AdLoader,
        private val interstitialLoader: InterstitialLoader,
        @Named("debugMode") private val debug: Boolean) : ViewModel(), LifecycleObserver {

    val showAd = ObservableBoolean(false)
    private val showConsent = MutableLiveData<Boolean>()
    private var serveAds = AtomicBoolean(false) // Let's give the user no Ads by default

    init {
        lifeCycle.addObserver(this)
    }

    fun consent(): LiveData<Boolean> = showConsent

    fun provideAdd(view: AdView) {
        if (!debug) {
            adLoader.loadAd(view, object : AdLoader.AdListener {
                override fun onAdLoaded() {
                    showAd.set(true)
                }

                override fun onAdFailed() {
                    showAd.set(false)
                }
            })
        }
    }

    fun provideInterstitial() {
        if (!debug && serveAds.get()) {
            interstitialLoader.showInterstitial() //// TODO entreco - 2019-06-08: nonPersonalized.get())
        }
    }

    private fun checkIfUserHasPurchasedItems() {
        connectToBillingUsecase.bind { connected ->
            if (connected) {
                fetchPurchasedItemsUsecase.exec(onPurchasesRetrieved(), onPurchasesError())
            }
        }
    }

    private fun onPurchasesRetrieved(): (FetchPurchasedItemsResponse) -> Unit {
        return { adResponse ->
            fetch.go { consentResponse ->
                showConsent.postValue(consentResponse.shouldAskForConsent)
                serveAds.set(adResponse.serveAds)
            }
        }
    }

    private fun onPurchasesError(): (Throwable) -> Unit {
        return { err ->
            // TODO entreco - 2019-06-04: logger.w("Unable to fetchPurchasedItems, $err")
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun bind() {
        checkIfUserHasPurchasedItems()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun unbind() {
        connectToBillingUsecase.unbind()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun destroy() {
        lifeCycle.removeObserver(this)
    }
}