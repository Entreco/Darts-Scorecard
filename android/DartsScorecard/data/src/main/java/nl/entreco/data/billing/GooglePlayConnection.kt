package nl.entreco.data.billing

import android.app.Activity
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import nl.entreco.domain.beta.donations.MakePurchaseResponse

class GooglePlayConnection : PurchasesUpdatedListener {
    private var service: BillingClient? = null
    private var callback: (MakePurchaseResponse) -> Unit = {}

    fun onServiceDisconnected() {
        this.service?.endConnection()
        this.service = null
        this.callback(MakePurchaseResponse.Disconnected)
    }

    fun onServiceConnected(activity: Activity) {
        this.service = BillingClient.newBuilder(activity)
                .enablePendingPurchases()
                .setListener(this)
                .build().apply {
                    startConnection(object : BillingClientStateListener {
                        override fun onBillingSetupFinished(billingResult: BillingResult) {
                            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                                callback(MakePurchaseResponse.Connected)
                            }
                        }

                        override fun onBillingServiceDisconnected() {
                            // Try to restart the connection on the next request to
                            // Google Play by calling the startConnection() method.
                            callback(MakePurchaseResponse.Disconnected)
                        }
                    })
                }
    }

    fun setCallback(done: (MakePurchaseResponse) -> Unit) {
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
            callback.invoke(MakePurchaseResponse.Cancelled)
        } else {
            // Handle any other error codes.
            callback.invoke(MakePurchaseResponse.Error(billingResult.responseCode))
        }
    }

    private fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            // Grant entitlement to the user.
            callback.invoke(MakePurchaseResponse.Purchased(purchase.purchaseToken, purchase.sku, purchase.orderId))

            // Acknowledge the purchase if it hasn't already been acknowledged.
            if (!purchase.isAcknowledged) {
                acknowledge(purchase.purchaseToken)
            } else {
                callback.invoke(MakePurchaseResponse.Acknowledged)
            }
        } else if (purchase.purchaseState == Purchase.PurchaseState.PENDING) {
            // Here you can confirm to the user that they've started the pending
            // purchase, and to complete it, they should follow instructions that
            // are given to them. You can also choose to remind the user in the
            // future to complete the purchase if you detect that it is still
            // pending.
            callback.invoke(MakePurchaseResponse.Pending)
        } else {
            // State is Purchase.PurchaseState.UNSPECIFIED_STATE
            // TODO: determine what to do here.
            // For now -> handle as Pending -> votes will be added.
            callback.invoke(MakePurchaseResponse.Pending)
        }
    }

    fun acknowledge(purchaseToken: String, updater: (MakePurchaseResponse) -> Unit = callback) {
        val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                .setPurchaseToken(purchaseToken)
                .build()

        service?.acknowledgePurchase(acknowledgePurchaseParams) { result ->
            when (val code = result.responseCode) {
                BillingClient.BillingResponseCode.OK -> updater.invoke(MakePurchaseResponse.Acknowledged)
                else                                 -> updater.invoke(MakePurchaseResponse.Error(code))
            }
        }
    }
}