package nl.entreco.data.billing

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.UiThread
import android.support.annotation.WorkerThread
import com.google.gson.GsonBuilder
import nl.entreco.domain.beta.Donation
import nl.entreco.domain.beta.donations.MakeDonationResponse
import nl.entreco.domain.repository.BillingRepository

/**
 * Created by entreco on 08/02/2018.
 */
class PlayStoreBillingRepository(private val context: Context, private val service: BillingServiceConnection) : BillingRepository {

    companion object {
        private const val BILLING_RESPONSE_RESULT_OK = 0
        private const val BILLING_INTENT = "com.android.vending.billing.InAppBillingService.BIND"
        private const val BILLING_PACKAGE = "com.android.vending"
        private const val FETCH_RESPONSE_CODE = "RESPONSE_CODE"
        private const val EXTRA_DETAILS_LIST = "DETAILS_LIST"
        private const val BUY_RESPONSE_CODE = "RESPONSE_CODE"
        private const val EXTRA_BUY_INTENT = "BUY_INTENT"
        private const val QUERY_RESPONSE_CODE = "RESPONSE_CODE"
        private const val EXTRA_INAPP_PURCHASE_ITEM_LIST = "INAPP_PURCHASE_ITEM_LIST"
    }

    private val gson by lazy { GsonBuilder().create() }
    private val apiVersion = 5
    private val packageName = context.packageName

    @UiThread
    override fun bind(done: (Boolean) -> Unit) {
        service.setCallback(done)
        val intent = Intent(BILLING_INTENT)
        intent.`package` = BILLING_PACKAGE
        context.bindService(intent, service, Context.BIND_AUTO_CREATE)
    }

    @UiThread
    override fun unbind() {
        service.setCallback({})
        context.unbindService(service)
    }

    @WorkerThread
    override fun fetchDonations(): List<Donation> {

        val donations = FetchDonationsData()
        val bundle = service.getService()?.getSkuDetails(apiVersion, packageName, donations.type(), donations.skuBundle())

        return if (bundle?.getInt(FETCH_RESPONSE_CODE) == BILLING_RESPONSE_RESULT_OK) {

            bundle.getStringArrayList(EXTRA_DETAILS_LIST).mapNotNull { response ->
                val donation = gson.fromJson(response, DonationApiData::class.java)
                val votes = donations.getVotes(donation.productId)

                Donation(donation.title, donation.description, donation.productId, donation.price, votes, donation.priceCurrencyCode, donation.priceAmountMicros)
            }.filter { donations.contains(it.sku) }
        } else {
            throw Throwable("Unable to retrieve donations, $bundle")
        }
    }

    @WorkerThread
    override fun donate(donation: Donation): MakeDonationResponse {
        val buy = MakeDonationData(donation)
        val payload = buy.payload()
        val bundle = service.getService()?.getBuyIntent(apiVersion, packageName, buy.sku(), buy.type(), payload)

        return if (bundle?.getInt(BUY_RESPONSE_CODE) == BILLING_RESPONSE_RESULT_OK) {
            val intent: PendingIntent = bundle.getParcelable(EXTRA_BUY_INTENT)

            MakeDonationResponse(intent, payload)
        } else {
            throw Throwable("Unable to getBuyIntent() $bundle")
        }
    }

    @WorkerThread
    override fun consume(token: String): Int {
        return service.getService()?.consumePurchase(apiVersion, packageName, token)!!
    }

    @WorkerThread
    override fun fetchPurchasedItems(): List<String> {
        val purchases = FetchPurchasesData()
        val bundle = service.getService()?.getPurchases(apiVersion, packageName, purchases.type(), purchases.token())

        return if (bundle?.getInt(QUERY_RESPONSE_CODE) == BILLING_RESPONSE_RESULT_OK) {
            return bundle.getStringArrayList(EXTRA_INAPP_PURCHASE_ITEM_LIST)
        } else {
            throw Throwable("Unable to getPurchases(), $bundle")
        }
    }
}
