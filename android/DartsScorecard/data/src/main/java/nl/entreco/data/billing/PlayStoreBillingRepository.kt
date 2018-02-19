package nl.entreco.data.billing

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
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

    private val BILLING_RESPONSE_RESULT_OK = 0

    private val gson by lazy { GsonBuilder().create() }
    private val apiVersion = 5
    private val packageName = context.packageName

    @UiThread
    override fun bind(done: (Boolean) -> Unit) {
        service.setCallback(done)
        val intent = Intent("com.android.vending.billing.InAppBillingService.BIND")
        intent.`package` = "com.android.vending"
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

        return if (bundle?.getInt("RESPONSE_CODE") == BILLING_RESPONSE_RESULT_OK) {

            bundle.getStringArrayList("DETAILS_LIST").mapNotNull { response ->
                val donation = gson.fromJson(response, DonationApiData::class.java)
                val votes = donations.getVotes(donation.productId)

                Donation(donation.title, donation.description, donation.productId, donation.price, votes)
            }.filter { donations.contains(it.sku) }
        } else {
            throw Throwable("Unable to retrieve donations, $bundle")
        }
    }

    override fun donate(donation: Donation): MakeDonationResponse {
        val buy = MakeDonationData(donation)
        val payload = buy.payload()
        val bundle = service.getService()?.run {
            getBuyIntent(apiVersion, packageName, buy.sku(), buy.type(), payload)
        }

        return if (bundle?.getInt("RESPONSE_CODE") == BILLING_RESPONSE_RESULT_OK) {
            val intent: PendingIntent = bundle.getParcelable("BUY_INTENT")

            MakeDonationResponse(intent, payload)
        } else {
            throw Throwable("oops, $bundle")
        }
    }

    override fun consume(token: String): Int {
        return service.getService()?.run {
            consumePurchase(apiVersion, packageName, token)
        }!!
    }
}
