package nl.entreco.dartsscorecard.beta.donate

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LifecycleObserver
import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.domain.Analytics
import nl.entreco.domain.beta.Donation
import nl.entreco.domain.beta.donations.Purchase
import nl.entreco.libcore.LiveEvent
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject

/**
 * Created by entreco on 08/02/2018.
 */
class DonateViewModel @Inject constructor(private val analytics: Analytics) : BaseViewModel(), LifecycleObserver {

    val donations = ObservableArrayList<Donation>()
    val loading = ObservableBoolean(true)
    val canRemoveAds = ObservableBoolean(false)

    private val events = LiveEvent<DonationEvent>()
    fun events(): LiveEvent<DonationEvent> = events

    private val skuToPurchase = AtomicReference<String>()

    fun onDonate(donation: Donation) {
        loading.set(true)
        analytics.trackPurchaseStart(donation)
        skuToPurchase.set(donation.sku)
        events.postValue(DonationEvent.Purchase(donation))
    }

    fun showDonations(_donations: List<Donation>) {
        loading.set(false)
        donations.clear()
        donations.addAll(_donations.sortedBy { it.priceMicros })
    }

    fun onCancelled(step: String) {
        loading.set(false)
        analytics.trackPurchaseFailed(skuToPurchase.getAndSet(""), step)
    }

    fun onDonated(donation: Donation, orderId: String) {
        loading.set(false)
        analytics.trackPurchase(donation, orderId)
    }

    fun onUpdate(purchases: List<Purchase>) {
        canRemoveAds.set(purchases.any { it.state == 1 })
    }
}
