package nl.entreco.domain.repository

import android.support.annotation.UiThread
import android.support.annotation.WorkerThread
import nl.entreco.domain.beta.Donation
import nl.entreco.domain.beta.donations.MakeDonationResponse

/**
 * Created by entreco on 08/02/2018.
 */
interface BillingRepository {
    @UiThread
    fun bind(done: (Boolean) -> Unit)

    @UiThread
    fun unbind()

    @WorkerThread
    fun fetchDonationsExclAds(): List<Donation>

    @WorkerThread
    fun fetchDonationsInclAds(): List<Donation>

    @WorkerThread
    fun donate(donation: Donation) : MakeDonationResponse

    @WorkerThread
    fun consume(token: String): Int

    @WorkerThread
    fun fetchPurchasedItems(): List<String>
}