package nl.entreco.data.billing

import android.app.Activity
import androidx.annotation.UiThread
import androidx.annotation.WorkerThread
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.ConsumeParams
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.SkuDetails
import com.android.billingclient.api.SkuDetailsParams
import nl.entreco.domain.beta.Donation
import nl.entreco.domain.beta.donations.MakeDonationResponse
import nl.entreco.domain.repository.BillingRepository

class PlayStoreBillingRepository(
        private val activityContext: Activity,
        private val playConnection: GooglePlayConnection
) : BillingRepository {

    private val productList: MutableMap<String, SkuDetails> = mutableMapOf()

    override fun bind(done: (Boolean) -> Unit) {
        playConnection.setCallback(done)
        playConnection.onServiceConnected(activityContext)
    }

    override fun unbind() {
        playConnection.setCallback { }
        playConnection.onServiceDisconnected()
    }

    @WorkerThread
    override fun fetchDonationsExclAds(done: (List<Donation>) -> Unit) {
        val donations = FetchDonationsData()
        return fetchProducts(donations, done)
    }

    @WorkerThread
    override fun fetchDonationsInclAds(done: (List<Donation>) -> Unit) {
        val donations = FetchDonationsInclAdsData()
        return fetchProducts(donations, done)
    }

    @WorkerThread
    private fun fetchProducts(donations: InAppProducts, done: (List<Donation>) -> Unit) {
        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(donations.listOfProducts()).setType(BillingClient.SkuType.INAPP)
        playConnection.getClient()?.querySkuDetailsAsync(params.build()) { result, skuDetailsList ->
            if (result.responseCode == BillingClient.BillingResponseCode.OK && skuDetailsList != null) {
                val items = skuDetailsList.map { details ->
                    val votes = donations.getVotes(details.sku)
                    val donation = Donation(details.title, details.description, details.sku, details.price, votes, details.priceCurrencyCode, details.priceAmountMicros)
                    productList.putIfAbsent(details.sku, details)
                    donation
                }
                done(items)
            } else {
                throw Throwable("Unable to retrieve donations, $params")
            }
        }
    }

    @UiThread
    override fun donate(donation: Donation, update: (MakeDonationResponse) -> Unit) {

        playConnection.donation(update)

        // Retrieve a value for "skuDetails" by calling querySkuDetailsAsync().
        val flowParams = BillingFlowParams.newBuilder()
                .setSkuDetails(productList[donation.sku])
                .build()

        val responseCode = playConnection.getClient()?.launchBillingFlow(activityContext, flowParams)
        when (val code = responseCode?.responseCode) {
            BillingClient.BillingResponseCode.OK                 -> update(MakeDonationResponse.Success)
            BillingClient.BillingResponseCode.USER_CANCELED      -> update(MakeDonationResponse.Cancelled)
            BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED -> update(MakeDonationResponse.AlreadyOwned)
            else                                                 -> update(MakeDonationResponse.Error(code))
        }
    }

    override fun consume(token: String, done: (Int) -> Unit) {
        val consumeParams = ConsumeParams.newBuilder()
                .setPurchaseToken(token)
                .build()
        playConnection.getClient()?.consumeAsync(consumeParams) { result, token ->
            done(result.responseCode)
        }
    }

    override fun acknowledge(token: String, done: (Int) -> Unit) {
        playConnection.acknowledge(token)
    }

    override fun fetchPurchasedItems(): List<String> {
        val purchases = FetchPurchasesData()
        val result = playConnection.getClient()?.queryPurchases(purchases.type())
        return if (result?.billingResult?.responseCode == BillingClient.BillingResponseCode.OK) {
            result.purchasesList.filter { it.purchaseState == Purchase.PurchaseState.PURCHASED }.map { it.sku }
        } else throw Throwable("Unable to getPurchases(), $result")
    }
}