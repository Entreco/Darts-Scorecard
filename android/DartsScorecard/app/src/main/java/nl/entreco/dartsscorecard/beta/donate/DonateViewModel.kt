package nl.entreco.dartsscorecard.beta.donate

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.databinding.ObservableArrayList
import android.util.Log
import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.domain.beta.ConnectToBillingUsecase
import nl.entreco.domain.beta.Donation
import javax.inject.Inject

/**
 * Created by entreco on 08/02/2018.
 */
class DonateViewModel @Inject constructor(private val lifeCycle: Lifecycle, private val connectToBillingUsecase: ConnectToBillingUsecase) : BaseViewModel(), LifecycleObserver {

    init {
        lifeCycle.addObserver(this)
    }

    val donations = ObservableArrayList<Donation>()

    private fun onDonationsSuccess(): (List<Donation>) -> Unit = { result ->
        donations.clear()
        donations.addAll(result)
    }

    private fun onDonationsFailed(): (Throwable) -> Unit = {
        Log.w("DONATE", "donationFailed: $it")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun bind() {
        connectToBillingUsecase.bind { connected ->
            connectToBillingUsecase.exec(onDonationsSuccess(), onDonationsFailed())
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