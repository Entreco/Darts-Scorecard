package nl.entreco.dartsscorecard.ad

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.databinding.ObservableBoolean
import com.google.android.gms.ads.AdView
import nl.entreco.dartsscorecard.BuildConfig
import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.dartsscorecard.di.viewmodel.ActivityScope
import nl.entreco.domain.ad.FetchPurchasedItemsResponse
import nl.entreco.domain.ad.FetchPurchasedItemsUsecase
import nl.entreco.domain.purchases.connect.ConnectToBillingUsecase
import nl.entreco.liblog.Logger
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import javax.inject.Named

@ActivityScope
class AdViewModel @Inject constructor(
        @ActivityScope private val lifeCycle: Lifecycle,
        @ActivityScope private val connectToBillingUsecase: ConnectToBillingUsecase,
        private val fetchPurchasedItemsUsecase: FetchPurchasedItemsUsecase,
        private val adLoader: AdLoader,
        private val interstitialLoader: InterstitialLoader,
        private val logger: Logger,
        @Named("debugMode") private val debug: Boolean = BuildConfig.DEBUG) : BaseViewModel(), LifecycleObserver {

    val showAd = ObservableBoolean(false)
    private var serveAds = AtomicBoolean(false) // Let's give the user no Ads by default

    init {
        lifeCycle.addObserver(this)
    }

    fun provideAdd(view: AdView) {
        if(!debug) {
            adLoader.loadAd(view, object : AdLoader.AdListener {
                override fun onAdLoaded() {
                    showAd.set(serveAds.get())
                }

                override fun onAdFailed() {
                    showAd.set(false)
                }
            })
        }
    }

    fun provideInterstitial() {
        if (!debug && serveAds.get()) {
            interstitialLoader.showInterstitial()
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
        return { response ->
            serveAds.set(response.serveAds)
        }
    }

    private fun onPurchasesError(): (Throwable) -> Unit {
        return { err ->
            logger.w("Unable to fetchPurchasedItems, $err")
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun bind() {
        checkIfUserHasPurchasedItems()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun unbind() {
        connectToBillingUsecase.unbind()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun destroy() {
        lifeCycle.removeObserver(this)
    }
}