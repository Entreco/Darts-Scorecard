package nl.entreco.data.billing

import android.app.Activity
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import nl.entreco.domain.beta.donations.MakeDonationResponse

class GooglePlayConnection : PurchasesUpdatedListener {
    private var service: BillingClient? = null

    private var callback: (Boolean) -> Unit = {}
    private var updater: (MakeDonationResponse) -> Unit = {}

    fun onServiceDisconnected() {
        this.service?.endConnection()
        this.service = null
        this.callback(false)
    }

    fun onServiceConnected(activity: Activity) {
        this.service = BillingClient.newBuilder(activity)
                .enablePendingPurchases()
                .setListener(this)
                .build().apply {
            startConnection(object : BillingClientStateListener {
                override fun onBillingSetupFinished(billingResult: BillingResult) {
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        callback(true)
                    }
                }

                override fun onBillingServiceDisconnected() {
                    // Try to restart the connection on the next request to
                    // Google Play by calling the startConnection() method.
                    callback(false)
                }
            })
        }
    }

    fun setCallback(done: (Boolean) -> Unit) {
        this.callback = done
    }

    fun getClient(): BillingClient? {
        return service
    }

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: MutableList<Purchase>?) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (purchase in purchases) {
                handlePurchase(purchase)
            }
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            // Handle an error caused by a user cancelling the purchase flow.
        } else {
            // Handle any other error codes.
        }
    }

    fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            // Grant entitlement to the user.
            updater.invoke(MakeDonationResponse.Purchased)

            // Acknowledge the purchase if it hasn't already been acknowledged.
            if (!purchase.isAcknowledged) {
                val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                        .setPurchaseToken(purchase.purchaseToken)
                        .build()
                service?.acknowledgePurchase(acknowledgePurchaseParams) { result ->
                    when (result.responseCode) {
                        BillingClient.BillingResponseCode.OK -> updater.invoke(MakeDonationResponse.Success)
                        else                                 -> updater.invoke(MakeDonationResponse.Unknown)
                    }
                }
            }
        } else if (purchase.purchaseState == Purchase.PurchaseState.PENDING) {
            // Here you can confirm to the user that they've started the pending
            // purchase, and to complete it, they should follow instructions that
            // are given to them. You can also choose to remind the user in the
            // future to complete the purchase if you detect that it is still
            // pending.
        }
    }

    fun donation(update: (MakeDonationResponse) -> Unit) {
        updater = update
    }
}