package nl.entreco.domain.repository

import androidx.annotation.UiThread
import androidx.annotation.WorkerThread
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
    fun fetchDonationsExclAds(done: (List<Donation>)->Unit)

    @WorkerThread
    fun fetchDonationsInclAds(done: (List<Donation>)->Unit)

    @WorkerThread
    fun donate(donation: Donation, update:(MakeDonationResponse)->Unit)

    @WorkerThread
    fun consume(token: String, done: (Int)->Unit)

    @WorkerThread
    fun fetchPurchasedItems(): List<String>
}