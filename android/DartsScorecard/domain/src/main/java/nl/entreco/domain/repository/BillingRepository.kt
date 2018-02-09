package nl.entreco.domain.repository

import android.support.annotation.UiThread
import android.support.annotation.WorkerThread
import nl.entreco.domain.beta.Donation

/**
 * Created by entreco on 08/02/2018.
 */
interface BillingRepository {
    @UiThread
    fun bind(done: (Boolean)->Unit)

    @UiThread
    fun unbind()

    @WorkerThread
    fun fetchDonations() : List<Donation>
}