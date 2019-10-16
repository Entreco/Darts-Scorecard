package nl.entreco.data.billing

import android.app.Activity
import androidx.annotation.UiThread
import androidx.annotation.WorkerThread
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ConsumeParams
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.SkuDetails
import com.android.billingclient.api.SkuDetailsParams
import nl.entreco.domain.beta.Donation
import nl.entreco.domain.beta.donations.MakePurchaseResponse
import nl.entreco.domain.repository.BillingRepository

class PlayStoreBillingRepository(
        private val activityContext: Activity,
        private val playConnection: GooglePlayConnection
) : BillingRepository {

    private val productList: MutableMap<String, SkuDetails> = mutableMapOf()

    override fun bind(done: (MakePurchaseResponse) -> Unit) {
        playConnection.addCallback(done)
        playConnection.onServiceConnected(activityContext)
    }

    override fun unbind(done: (MakePurchaseResponse) -> Unit) {
        playConnection.removeCallback(done)
        playConnection.onServiceDisconnected()
    }

    @WorkerThread
    override fun fetchDonationsExclAds(done: (List<Donation>) -> Unit, fail: (Throwable) -> Unit) {
        val donations = FetchDonationsData()
        fetchProducts(donations, done, fail)
    }

    @WorkerThread
    override fun fetchDonationsInclAds(done: (List<Donation>) -> Unit, fail: (Throwable) -> Unit) {
        val donations = FetchDonationsInclAdsData()
        fetchProducts(donations, done, fail)
    }

    @WorkerThread
    private fun fetchProducts(donations: InAppProducts, done: (List<Donation>) -> Unit, fail: (Throwable) -> Unit) {
        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(donations.listOfProducts()).setType(BillingClient.SkuType.INAPP)
        val client = playConnection.getClient()
        if (client == null) throw Throwable("Unable to retrieve donations, play client == null, $params")
        else {
            client.querySkuDetailsAsync(params.build()) { result, skuDetailsList ->
                if (result.responseCode == BillingClient.BillingResponseCode.OK && skuDetailsList != null) {
                    val items = skuDetailsList.map { details ->
                        val votes = donations.getVotes(details.sku)
                        val donation = Donation(details.title, details.description, details.sku, details.price, votes, details.priceCurrencyCode, details.priceAmountMicros)
                        productList.putIfAbsent(details.sku, details)
                        donation
                    }
                    done(items)
                } else {
                    fail(Throwable("Unable to retrieve donations, $params"))
                }
            }
        }
    }

    @UiThread
    override fun donate(donation: Donation, update: (MakePurchaseResponse) -> Unit) {
        playConnection.addCallback(update)
        // Retrieve a value for "skuDetails" by calling querySkuDetailsAsync().
        val flowParams = BillingFlowParams.newBuilder()
                .setSkuDetails(productList[donation.sku])
                .build()

        val responseCode = try {
            playConnection.getClient()?.launchBillingFlow(activityContext, flowParams)
        } catch (err: Throwable) {
            BillingResult.newBuilder().setResponseCode(BillingClient.BillingResponseCode.BILLING_UNAVAILABLE).build()
        }
        when (val code = responseCode?.responseCode) {
            BillingClient.BillingResponseCode.OK            -> update(MakePurchaseResponse.Launched)
            BillingClient.BillingResponseCode.USER_CANCELED -> update(MakePurchaseResponse.Cancelled)
            else                                            -> update(MakePurchaseResponse.Error(code
                    ?: -100))
        }
    }

    override fun consume(token: String, done: (MakePurchaseResponse) -> Unit) {
        val consumeParams = ConsumeParams.newBuilder()
                .setPurchaseToken(token)
                .build()
        playConnection.getClient()?.consumeAsync(consumeParams) { result, _ ->
            val response = when (result.responseCode) {
                BillingClient.BillingResponseCode.OK -> MakePurchaseResponse.Consumed
                else                                 -> MakePurchaseResponse.Error(result.responseCode)
            }
            done(response)
        }
    }

    override fun acknowledge(token: String) {
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