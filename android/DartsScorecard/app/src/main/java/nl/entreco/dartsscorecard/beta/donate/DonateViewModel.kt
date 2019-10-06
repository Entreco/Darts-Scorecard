package nl.entreco.dartsscorecard.beta.donate

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.domain.Analytics
import nl.entreco.domain.beta.Donation
import nl.entreco.domain.beta.donations.ConsumeDonationRequest
import nl.entreco.domain.beta.donations.ConsumeDonationResponse
import nl.entreco.domain.beta.donations.ConsumeDonationUsecase
import nl.entreco.domain.beta.donations.FetchDonationsResponse
import nl.entreco.domain.beta.donations.FetchDonationsUsecase
import nl.entreco.domain.beta.donations.MakeDonationRequest
import nl.entreco.domain.beta.donations.MakeDonationUsecase
import nl.entreco.domain.beta.donations.MakePurchaseResponse
import nl.entreco.domain.purchases.connect.ConnectToBillingUsecase
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject

/**
 * Created by entreco on 08/02/2018.
 */
class DonateViewModel @Inject constructor(
        private var donateCallback: DonateCallback?,
        private val connectToBillingUsecase: ConnectToBillingUsecase,
        private val fetchDonationsUsecase: FetchDonationsUsecase,
        private val makeDonation: MakeDonationUsecase,
        private val consumeDonation: ConsumeDonationUsecase,
        private val analytics: Analytics) : BaseViewModel(), LifecycleObserver {

    init {
        donateCallback?.lifeCycle()?.addObserver(this)
    }

    internal var productSku = AtomicReference<String>("")
    internal var requiresConsumption = AtomicBoolean(true)
    val donations = ObservableArrayList<Donation>()
    val loading = ObservableBoolean(false)
    val canRemoveAds = ObservableBoolean(false)

    fun onDonate(donation: Donation) {
        loading.set(true)
        productSku.set(donation.sku)
        analytics.trackPurchaseStart(donation)
        makeDonation.exec(MakeDonationRequest(donation), onStartMakeDonation(), onStartMakeDonationFailed())
    }

    private fun onStartMakeDonation(): (MakePurchaseResponse) -> Unit = { response ->
        donateCallback?.makeDonation(response)
    }

    fun onMakeDonationSuccess(data: MakePurchaseResponse.Purchased) {
        analytics.trackAchievement("Donation $data")
        consumeDonation.exec(ConsumeDonationRequest(data.purchaseToken, data.sku, data.orderId, requiresConsumption.get()),
                onConsumeDonationSuccess(),
                onConsumeDonationFailed())
    }

    private fun onStartMakeDonationFailed(): (Throwable) -> Unit = {
        analytics.trackPurchaseFailed(productSku.get(), "GetBuyIntent failed")
        loading.set(false)
        productSku.set("")
        requiresConsumption.set(false)
    }

    private fun onConsumeDonationSuccess(): (ConsumeDonationResponse) -> Unit = { response ->
        when (response) {
            is ConsumeDonationResponse.Success -> donationDone(response)
            is ConsumeDonationResponse.Error   -> analytics.trackPurchaseFailed(response.sku, "Consume failed ${response.errorCode}")
        }

        loading.set(false)
        productSku.set("")
        requiresConsumption.set(false)
        fetchDonationsUsecase.exec(onFetchDonationsSuccess(), onFetchDonationsFailed())
    }

    private fun donationDone(response: ConsumeDonationResponse.Success) {
        donationWithId(response)?.let { donation ->
            analytics.trackPurchase(donation, response.orderId)
            donateCallback?.onDonationMade(donation)
        }
    }

    private fun onConsumeDonationFailed(): (Throwable) -> Unit = {
        analytics.trackPurchaseFailed(productSku.get(), "ConsumeDonation failed")
        loading.set(false)
        productSku.set("")
        requiresConsumption.set(false)
    }

    private fun donationWithId(response: ConsumeDonationResponse.Success) = donations.firstOrNull { it.sku == response.sku }

    fun onMakeDonationFailed(msg: String) {
        analytics.trackPurchaseFailed(productSku.get(), msg)
        loading.set(false)
        productSku.set("")
        requiresConsumption.set(false)
    }

    private fun onFetchDonationsSuccess(): (FetchDonationsResponse) -> Unit = { result ->
        when (result) {
            is FetchDonationsResponse.Ok    -> {
                requiresConsumption.set(result.needToBeConsumed)
                canRemoveAds.set(!result.needToBeConsumed)
                donations.clear()
                donations.addAll(result.donations)
            }
            is FetchDonationsResponse.Error -> {
                requiresConsumption.set(result.hasPreviouslyBoughtItems)
                canRemoveAds.set(!result.hasPreviouslyBoughtItems)
                donations.clear()
                onFetchDonationsFailed().invoke(result.error)
            }
        }
    }

    private fun onFetchDonationsFailed(): (Throwable) -> Unit = {
        analytics.trackPurchaseFailed(productSku.get(), "FetchDonations failed")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun bind() {
        connectToBillingUsecase.bind { connected ->
            if (connected && donations.isEmpty()) {
                fetchDonationsUsecase.exec(onFetchDonationsSuccess(), onFetchDonationsFailed())
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun unbind() {
        connectToBillingUsecase.unbind()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun destroy() {
        donateCallback?.lifeCycle()?.removeObserver(this)
    }

    override fun onCleared() {
        donateCallback = null
        super.onCleared()
    }
}
