package nl.entreco.dartsscorecard.beta.donate

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.databinding.ObservableArrayList
import android.util.Log
import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.domain.beta.Donation
import nl.entreco.domain.beta.connect.ConnectToBillingUsecase
import nl.entreco.domain.beta.donations.FetchDonationsResponse
import nl.entreco.domain.beta.donations.FetchDonationsUsecase
import javax.inject.Inject

/**
 * Created by entreco on 08/02/2018.
 */
class DonateViewModel @Inject constructor(private val lifeCycle: Lifecycle,
                                          private val connectToBillingUsecase: ConnectToBillingUsecase,
                                          private val fetchDonationsUsecase: FetchDonationsUsecase) : BaseViewModel(), LifecycleObserver {

    init {
        lifeCycle.addObserver(this)
    }

    val donations = ObservableArrayList<Donation>()

    private fun onDonationsSuccess(): (FetchDonationsResponse) -> Unit = { result ->
        donations.clear()
        donations.addAll(result.donations)
    }

    private fun onDonationsFailed(): (Throwable) -> Unit = {
        Log.w("DONATE", "donationFailed: $it")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun bind() {
        connectToBillingUsecase.bind { connected ->
            fetchDonationsUsecase.exec(onDonationsSuccess(), onDonationsFailed())
        }
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