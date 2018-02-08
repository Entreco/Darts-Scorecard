package nl.entreco.data.billing

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.UiThread
import android.support.annotation.WorkerThread
import nl.entreco.domain.beta.Donation
import nl.entreco.domain.repository.BillingRepository
import org.json.JSONObject

/**
 * Created by entreco on 08/02/2018.
 */
class PlayStoreBillingRepository(private val context: Context, private val service: BillingServiceConnection) : BillingRepository {

    private val BILLING_RESPONSE_RESULT_OK = 0

    private val apiVersion = 3
    private val packageName = context.packageName
    private val skuType = "inapp"

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
        val bundle = service.getService()?.run {
            val skuList = arrayListOf("donations")
            val skuBundle = Bundle()
            skuBundle.putStringArrayList("ITEM_ID_LIST", skuList)
            getSkuDetails(apiVersion, packageName, skuType, skuBundle)
        }

        return if (bundle?.getInt("RESPONSE_CODE") == BILLING_RESPONSE_RESULT_OK) {
            bundle.getStringArrayList("DETAILS_LIST").mapNotNull { response ->
                val json = JSONObject(response)
                val sku = json.getString("productId")
                val price = json.getString("price")
                when (sku) {
                    "donations" -> Donation("title", "description", sku, price)
                    else -> null
                }
            }
        } else {
            throw IllegalStateException("Unable to retrieve donations, $bundle")
        }
    }
}