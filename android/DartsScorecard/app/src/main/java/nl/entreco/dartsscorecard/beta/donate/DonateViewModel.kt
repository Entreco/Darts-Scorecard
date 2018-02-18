package nl.entreco.dartsscorecard.beta.donate

import android.app.Activity.RESULT_OK
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Intent
import android.databinding.ObservableArrayList
import android.databinding.ObservableBoolean
import android.util.Log
import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.domain.Analytics
import nl.entreco.domain.beta.Donation
import nl.entreco.domain.beta.connect.ConnectToBillingUsecase
import nl.entreco.domain.beta.donations.*
import javax.inject.Inject

/**
 * Created by entreco on 08/02/2018.
 */
class DonateViewModel @Inject constructor(
        private val donateCallback: DonateCallback,
        private val connectToBillingUsecase: ConnectToBillingUsecase,
        private val fetchDonationsUsecase: FetchDonationsUsecase,
        private val makeDonation: MakeDonationUsecase,
        private val consumeDonation: ConsumeDonationUsecase,
        private val analytics: Analytics) : BaseViewModel(), LifecycleObserver {

    init {
        donateCallback.lifeCycle().addObserver(this)
        analytics.trackViewDonations()
    }

    internal var productId: String = ""
    val donations = ObservableArrayList<Donation>()
    val loading = ObservableBoolean(false)

    fun onDonate(donation: Donation) {
        loading.set(true)
        productId = donation.sku
        makeDonation.exec(MakeDonationRequest(donation), onStartMakeDonation(), onStartMakeDonationFailed())
    }

    private fun onStartMakeDonation(): (MakeDonationResponse) -> Unit = { response ->
        donateCallback.makeDonation(response)
    }

    fun onMakeDonationSuccess(data: Intent?) {
        Log.w("DONATE", "onMakeDonationSuccess: $data")
        val purchaseData = data!!.getStringExtra("INAPP_PURCHASE_DATA")
        val dataSignature = data.getStringExtra("INAPP_DATA_SIGNATURE")
        consumeDonation.exec(ConsumeDonationRequest(purchaseData, dataSignature), onConsumeDonationSuccess(), onConsumeDonationFailed())
    }


    private fun onStartMakeDonationFailed(): (Throwable) -> Unit = {
        analytics.trackPurchaseFailed(productId, "GetBuyIntent failed")
        loading.set(false)
        productId = ""
    }

    private fun onConsumeDonationSuccess(): (ConsumeDonationResponse) -> Unit = { response ->
        when(response.resultCode){
            RESULT_OK -> donationDone(response)
            else -> analytics.trackPurchaseFailed(response.productId, "Consume failed")
        }

        loading.set(false)
        productId = ""
    }

    private fun donationDone(response: ConsumeDonationResponse) {
        donationWithId(response)?.let { donation ->
            analytics.trackPurchase(donation)
            donateCallback.onDonationMade(donation)
        }
    }

    private fun onConsumeDonationFailed(): (Throwable) -> Unit = {
        Log.w("DONATE", "consumeFailed: $it")
        analytics.trackPurchaseFailed(productId, "Consume failed")
        loading.set(false)
        productId = ""
    }


    private fun donationWithId(response: ConsumeDonationResponse): Donation? =
            donations.firstOrNull { it.sku == response.productId }

    fun onMakeDonationFailed(resultCode: Int, data: Intent?) {
        Log.w("DONATE", "onMakeDonationFailed: $resultCode $data")
        analytics.trackPurchaseFailed(productId, "ActivityResult failed")
        loading.set(false)
        productId = ""
    }

    private fun onFetchDonationsSuccess(): (FetchDonationsResponse) -> Unit = { result ->
        donations.clear()
        donations.addAll(result.donations)
    }

    private fun onFetchDonationsFailed(): (Throwable) -> Unit = {
        Log.w("DONATE", "donationFailed: $it")
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
        donateCallback.lifeCycle().removeObserver(this)
    }

}