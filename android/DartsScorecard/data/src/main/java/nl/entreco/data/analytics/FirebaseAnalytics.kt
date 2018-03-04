package nl.entreco.data.analytics

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import nl.entreco.domain.Analytics
import nl.entreco.domain.beta.Donation
import nl.entreco.domain.beta.Feature


/**
 * Created by Entreco on 15/11/2017.
 */
class FirebaseAnalytics(context: Context) : Analytics {

    private val fb by lazy { FirebaseAnalytics.getInstance(context) }

    override fun trackScore(scored: String, total: Int) {
        fb.logEvent("scored", Bundle().apply {
            putString(FirebaseAnalytics.Param.SCORE, scored)
            putInt(FirebaseAnalytics.Param.VALUE, total)
        })
    }

    override fun trackAchievement(achievementId: String) {
        fb.logEvent(FirebaseAnalytics.Event.UNLOCK_ACHIEVEMENT, Bundle().apply {
            putString(FirebaseAnalytics.Param.ACHIEVEMENT_ID, achievementId)
        })
    }

    override fun setFavDoubleProperty(favouriteDouble: String) {
        fb.setUserProperty("fav_double", favouriteDouble)
    }

    override fun trackViewFeature(feature: Feature) {
        fb.logEvent(FirebaseAnalytics.Event.ADD_TO_WISHLIST, Bundle().apply {
            putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "feature")
            putInt(FirebaseAnalytics.Param.QUANTITY, 1)
            putString(FirebaseAnalytics.Param.ITEM_NAME, feature.title)
            putString(FirebaseAnalytics.Param.ITEM_ID, feature.ref)
        })
    }

    override fun trackPurchaseStart(donation: Donation) {
        val ecommerceBundle = toBundle(donation)

        // Set checkout step and optional checkout option
        ecommerceBundle.putLong(FirebaseAnalytics.Param.CHECKOUT_STEP, 1) // Optional for first step
        fb.logEvent(FirebaseAnalytics.Event.BEGIN_CHECKOUT, ecommerceBundle)
    }

    override fun trackPurchase(donation: Donation, orderId: String) {
        trackAchievement("Made Donation")

        val ecommerceBundle = toBundle(donation)
        ecommerceBundle.putString(FirebaseAnalytics.Param.TRANSACTION_ID, orderId)
        ecommerceBundle.putString(FirebaseAnalytics.Param.AFFILIATION, "Entreco - DartsApp")
        ecommerceBundle.putDouble(FirebaseAnalytics.Param.VALUE, formatMicros(donation.priceMicros))
        ecommerceBundle.putString(FirebaseAnalytics.Param.CURRENCY, donation.priceCurrencyCode)

        fb.logEvent(FirebaseAnalytics.Event.ECOMMERCE_PURCHASE, ecommerceBundle)
    }

    override fun trackPurchaseFailed(productId: String, step: String) {
        fb.logEvent(FirebaseAnalytics.Event.CHECKOUT_PROGRESS, Bundle()
                .apply {
                    putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "donations")
                    putString(FirebaseAnalytics.Param.CHECKOUT_STEP, step)
                    putString(FirebaseAnalytics.Param.ITEM_ID, productId)
                })
    }

    private fun toBundle(donation: Donation): Bundle {
        val product = Bundle().apply {
            putString(FirebaseAnalytics.Param.ITEM_ID, donation.sku) // ITEM_ID or ITEM_NAME is required
            putString(FirebaseAnalytics.Param.ITEM_NAME, donation.title)
            putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "Donations")
            putString(FirebaseAnalytics.Param.ITEM_VARIANT, donation.price)
            putString(FirebaseAnalytics.Param.ITEM_BRAND, "Entreco")
            putDouble(FirebaseAnalytics.Param.PRICE, formatMicros(donation.priceMicros))
            putString(FirebaseAnalytics.Param.CURRENCY, donation.priceCurrencyCode) // Item-level currency unused today
            putLong(FirebaseAnalytics.Param.QUANTITY, donation.votes.toLong())
        }

        val items = ArrayList<Bundle>()
        items.add(product)

        val ecommerceBundle = Bundle()
        ecommerceBundle.putParcelableArrayList("items", items)
        return ecommerceBundle
    }

    internal fun formatMicros(priceMicros: String): Double {
        return try {
            priceMicros.toDouble() / 1000000
        } catch (invalidFormat: NumberFormatException) {
            0.0
        }
    }
}
