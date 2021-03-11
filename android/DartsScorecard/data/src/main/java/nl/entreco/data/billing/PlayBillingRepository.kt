package nl.entreco.data.billing

import android.app.Activity
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClient.SkuType
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ConsumeParams
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.Purchase.PurchasesResult
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.SkuDetails
import com.android.billingclient.api.SkuDetailsParams
import nl.entreco.domain.beta.Donation
import nl.entreco.domain.beta.donations.MakePurchaseResponse
import nl.entreco.domain.repository.BillingRepo
import nl.entreco.liblog.Logger
import java.lang.ref.WeakReference
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger


class PlayBillingRepository(
        private val reference: WeakReference<Activity>,
        private val logger: Logger,
        private var listener: (MakePurchaseResponse) -> Unit
) : BillingRepo {

    companion object {
        private const val BILLING_MANAGER_NOT_INITIALIZED = -1001
    }

    private val serviceConnected = AtomicBoolean(false)
    private val billingClientResponseCode = AtomicInteger(BILLING_MANAGER_NOT_INITIALIZED)
    private val skuDetails: MutableMap<String, SkuDetails?> = mutableMapOf()
    private val tokensToBeConsumed: MutableSet<String> = mutableSetOf()

    private val purchaseListener = PurchasesUpdatedListener { billingResult, purchases ->
        when (billingResult?.responseCode) {
            BillingClient.BillingResponseCode.OK            -> {
                purchases?.forEach { purchase ->
                    handlePurchase(purchase)
                }

                val donations = purchases?.map { nl.entreco.domain.beta.donations.Purchase(it.sku, it.isAcknowledged, it.purchaseState) } ?: emptyList()
                listener(MakePurchaseResponse.Updated(donations))
            }
            BillingClient.BillingResponseCode.USER_CANCELED -> {
                logger.i("IAB", "onPurchasesUpdated() - user cancelled the purchase flow - skipping")
                listener(MakePurchaseResponse.Cancelled)
            }
            else                                            -> {
                logger.w("IAB", "onPurchasesUpdated() got unknown resultCode: ${billingResult?.responseCode}")
                listener(MakePurchaseResponse.Unknown)
            }
        }
    }

    private val client = BillingClient.newBuilder(reference.get()!!).setListener(purchaseListener).enablePendingPurchases().build()

    override fun start() {
        startServiceConnection {
            listener(MakePurchaseResponse.Connected)
            // IAB is fully set up. Now, let's get an inventory of stuff we own.
            logger.d("IAB", "Setup successful. Querying inventory.")
            fetchPurchases()
        }
    }

    override fun resume() {
        if (billingClientResponseCode.get() == BillingClient.BillingResponseCode.OK) {
            fetchPurchases()
        }
    }

    private fun startServiceConnection(executeOnSuccess: () -> Unit) {
        client.startConnection(object : BillingClientStateListener {
            override fun onBillingServiceDisconnected() {
                serviceConnected.set(false)
                listener(MakePurchaseResponse.Disconnected)
            }

            override fun onBillingSetupFinished(billingResult: BillingResult) {

                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    serviceConnected.set(true)
                    executeOnSuccess()
                }

                billingClientResponseCode.set(billingResult.responseCode)
            }
        })
    }

    override fun purchase(skuId: String) {
        executeServiceRequest {
            reference.get()?.let { ctx ->
                skuDetails.getOrDefault(skuId, null)?.let { detail ->
                    val purchaseParams = BillingFlowParams.newBuilder().setSkuDetails(detail).build()
                    client.launchBillingFlow(ctx, purchaseParams)
                }
            }

        }
    }

    /**
     * Handles the purchase
     *
     * Note: Notice that for each purchase, we check if signature is valid on the client.
     * It's recommended to move this check into your backend.
     * See [Security.verifyPurchase]
     *
     * @param purchase Purchase to be handled
     */
    private fun handlePurchase(purchase: Purchase) {
        if (!verifyValidSignature(purchase.originalJson, purchase.signature)) {
            logger.i("IAB", "Got a purchase: $purchase but signature is bad. Skipping...")
            return
        }

        logger.d("IAB", "Got a verified purchase: $purchase")

        when {
            purchase.purchaseState == Purchase.PurchaseState.PURCHASED ->
                // Grant entitlement to the user.
                skuDetails.getOrDefault(purchase.sku, null)?.let { details ->

                    val donation = Donation(details.title, details.description, details.sku, details.price, details.getVotes(), details.priceCurrencyCode, details.priceAmountMicros)
                    listener(MakePurchaseResponse.Purchased(donation, purchase.orderId))


                    // Acknowledge the purchase if it hasn't already been acknowledged.
                    if (!purchase.isAcknowledged) {
                        val permanentPurchase = FetchDonationsInclAdsData().contains(purchase.sku) || FetchDonationsInclAdsTestData().contains(purchase.sku)
                        if (permanentPurchase) {
                            // Acknowledge
                            acknowledge(purchase)
                        } else {
                            // Consume
                            consume(purchase)
                        }
                    }
                }
            purchase.purchaseState == Purchase.PurchaseState.PENDING   ->
                // Here you can confirm to the user that they've started the pending
                // purchase, and to complete it, they should follow instructions that
                // are given to them. You can also choose to remind the user in the
                // future to complete the purchase if you detect that it is still
                // pending.
                listener(MakePurchaseResponse.Pending(purchase.sku))
            else                                                       ->
                // State is Purchase.PurchaseState.UNSPECIFIED_STATE
                // TODO: determine what to do here.
                // For now -> handle as Pending -> votes will be added.
                listener(MakePurchaseResponse.Unknown)
        }
    }

    private fun acknowledge(purchase: Purchase) {

        if (tokensToBeConsumed.contains(purchase.purchaseToken)) {
            logger.i("IAB", "Token was already scheduled to be consumed - skipping...")
            return
        }
        tokensToBeConsumed.add(purchase.purchaseToken)

        val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                .setPurchaseToken(purchase.purchaseToken)
                .build()
        client.acknowledgePurchase(acknowledgePurchaseParams) { result ->
            if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                logger.i("IAB", "Acknowledged purchase OK: $purchase")
                listener(MakePurchaseResponse.Acknowledged)
            } else {
                logger.i("IAB", "Acknowledged purchase FAILED: $purchase, result: $result")
            }
        }
    }

    private fun consume(purchase: Purchase) {

        if (tokensToBeConsumed.contains(purchase.purchaseToken)) {
            logger.i("IAB", "Token was already scheduled to be consumed - skipping...")
            return
        }
        tokensToBeConsumed.add(purchase.purchaseToken)

        val consumeParams = ConsumeParams.newBuilder()
                .setPurchaseToken(purchase.purchaseToken)
                .build()
        client.consumeAsync(consumeParams) { result, token ->
            if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                logger.i("IAB", "Consumed purchase OK: $purchase")
                listener(MakePurchaseResponse.Consumed)
            } else {
                logger.i("IAB", "Consumed purchase FAILED: $purchase, result: $result")
            }
        }
    }

    override fun fetchPurchases() {
        executeServiceRequest {
            val time = System.currentTimeMillis()
            val purchasesResult = client.queryPurchases(SkuType.INAPP)
            logger.i("IAB", "Querying purchases elapsed time: ${System.currentTimeMillis() - time}ms")

            onQueryPurchasesFinished(purchasesResult)

            val donations = if (purchasesResult.purchasesList?.any { it.purchaseState == Purchase.PurchaseState.PURCHASED } == true) {
                // Needs to be consumed
                FetchDonationsData()
            } else {
                // Needs to be acknowledged
                FetchDonationsInclAdsData()
            }

            val params = SkuDetailsParams.newBuilder().setSkusList(donations.listOfProducts()).setType(SkuType.INAPP)
            client.querySkuDetailsAsync(params.build()) { result, skuDetailsList ->
                if (result.responseCode == BillingClient.BillingResponseCode.OK && skuDetailsList != null) {
                    val items = skuDetailsList.map { details ->
                        val donation = Donation(details.title, details.description, details.sku, details.price, details.getVotes(), details.priceCurrencyCode, details.priceAmountMicros)
                        skuDetails.putIfAbsent(details.sku, details)
                        donation
                    }
                    listener(MakePurchaseResponse.Donations(items))
                } else {
                    listener(MakePurchaseResponse.Unavailable)
                }
            }
        }
    }

    /**
     * Handle a result from querying of purchases and report an updated list to the listener
     */
    private fun onQueryPurchasesFinished(result: PurchasesResult) {
        // Have we been disposed of in the meantime? If so, or bad result code, then quit
        if (client == null || result.responseCode != BillingClient.BillingResponseCode.OK) {
            logger.w("IAB", "Billing client was null or result code (${result.responseCode}) was bad - quitting")
            return
        }

        logger.d("IAB", "Query inventory was successful. ${result.purchasesList ?: emptyList()}")

        // Update the UI and purchases inventory with new list of purchases
        tokensToBeConsumed.clear()
        purchaseListener.onPurchasesUpdated(BillingResult.newBuilder().setResponseCode(BillingClient.BillingResponseCode.OK).build(), result.purchasesList ?: emptyList())
    }

    private fun executeServiceRequest(runnable: () -> Unit) {
        if (serviceConnected.get()) {
            runnable()
        } else {
            // If billing service was disconnected, we try to reconnect 1 time.
            // (feel free to introduce your retry policy here).
            startServiceConnection(runnable)
        }
    }

    /**
     * Verifies that the purchase was signed correctly for this developer's public key.
     *
     * Note: It's strongly recommended to perform such check on your backend since hackers can
     * replace this method with "constant true" if they decompile/rebuild your app.
     *
     */
    private fun verifyValidSignature(signedData: String, signature: String): Boolean {
        // Some sanity checks to see if the developer (that's you!) really followed the
        // instructions to run this sample (don't put these checks on your app!)
//        if (BASE_64_ENCODED_PUBLIC_KEY.contains("CONSTRUCT_YOUR")) {
//            throw RuntimeException("Please update your app's public key at: " + "BASE_64_ENCODED_PUBLIC_KEY")
//        }
//
//        try {
//            return Security.verifyPurchase(BASE_64_ENCODED_PUBLIC_KEY, signedData, signature)
//        } catch (e: IOException) {
//            logger.e(TAG, "Got an exception trying to validate a purchase: $e")
//            return false
//        }
        return true

    }


    override fun stop() {
        if (client.isReady) {
            client.endConnection()
        }
        reference.clear()
        listener = {}
    }
}

fun SkuDetails.getVotes(): Int {
    val inclAds = FetchDonationsInclAdsData()
    val inclTest = FetchDonationsInclAdsTestData()
    val exclAds = FetchDonationsData()
    val exclTest = FetchDonationsTestData()
    return when {
        inclAds.contains(sku)  -> inclAds.getVotes(sku)
        exclAds.contains(sku)  -> exclAds.getVotes(sku)
        inclTest.contains(sku) -> inclTest.getVotes(sku)
        exclTest.contains(sku) -> exclTest.getVotes(sku)
        else                   -> 10
    }
}