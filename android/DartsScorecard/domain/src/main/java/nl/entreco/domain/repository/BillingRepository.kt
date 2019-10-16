package nl.entreco.domain.repository

import androidx.annotation.UiThread
import androidx.annotation.WorkerThread
import nl.entreco.domain.beta.Donation
import nl.entreco.domain.beta.donations.MakePurchaseResponse

/**
 * Created by entreco on 08/02/2018.
 */
interface BillingRepository {
    @UiThread
    fun bind(done: (MakePurchaseResponse) -> Unit)

    @UiThread
    fun unbind(done: (MakePurchaseResponse) -> Unit)

    @WorkerThread
    fun fetchDonationsExclAds(done: (List<Donation>) -> Unit, fail: (Throwable) -> Unit)

    @WorkerThread
    fun fetchDonationsInclAds(done: (List<Donation>) -> Unit, fail: (Throwable) -> Unit)

    @WorkerThread
    fun donate(donation: Donation, update: (MakePurchaseResponse) -> Unit)

    @WorkerThread
    fun consume(token: String, done: (MakePurchaseResponse) -> Unit)

    @WorkerThread
    fun acknowledge(token: String)

    @WorkerThread
    fun fetchPurchasedItems(): List<String>
}