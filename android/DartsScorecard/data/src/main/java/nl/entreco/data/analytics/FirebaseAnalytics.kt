package nl.entreco.data.analytics

import android.content.Context
import android.os.Bundle
import nl.entreco.domain.Analytics
import nl.entreco.domain.beta.Donation
import nl.entreco.domain.beta.Feature
import nl.entreco.domain.wtf.WtfItem

typealias Fa = com.google.firebase.analytics.FirebaseAnalytics
typealias FaEvent = com.google.firebase.analytics.FirebaseAnalytics.Event
typealias FaParam = com.google.firebase.analytics.FirebaseAnalytics.Param

/**
 * Created by Entreco on 15/11/2017.
 */
class FirebaseAnalytics(context: Context) : Analytics {

    private val fb by lazy { Fa.getInstance(context) }

    override fun trackScore(scored: String, total: Int) {
        fb.logEvent("scored", trackScoreBundle(scored, total))
    }

    internal fun trackScoreBundle(scored: String, total: Int): Bundle {
        return Bundle().apply {
            putString(FaParam.SCORE, scored)
            putInt(FaParam.VALUE, total)
        }
    }

    override fun trackAchievement(achievementId: String) {
        fb.logEvent(FaEvent.UNLOCK_ACHIEVEMENT, trackAchievementBundle(achievementId))
    }

    internal fun trackAchievementBundle(achievementId: String): Bundle {
        return Bundle().apply {
            putString(FaParam.ACHIEVEMENT_ID, achievementId)
        }
    }

    override fun setFavDoubleProperty(favouriteDouble: String) {
        fb.setUserProperty("fav_double", favouriteDouble)
    }

    override fun trackViewFeature(feature: Feature, amount: Int) {
        fb.logEvent(FaEvent.ADD_TO_WISHLIST, trackViewFeatureBundle(amount, feature))
    }

    internal fun trackViewFeatureBundle(amount: Int, feature: Feature): Bundle {
        return Bundle().apply {
            putString(FaParam.ITEM_CATEGORY, "feature")
            putInt(FaParam.QUANTITY, amount)
            putString(FaParam.ITEM_NAME, feature.title)
            putString(FaParam.ITEM_ID, feature.ref)
        }
    }

    override fun trackViewFaq(item: WtfItem) {
        fb.logEvent(FaEvent.VIEW_ITEM, trackViewFaqBundle(item))
    }

    internal fun trackViewFaqBundle(item: WtfItem): Bundle {
        return Bundle().apply {
            putString(FaParam.ITEM_ID, item.docId)
            putString(FaParam.ITEM_NAME, item.title)
            putString(FaParam.ITEM_CATEGORY, "faq")
        }
    }

    override fun trackPurchaseStart(donation: Donation) {
        val ecommerceBundle = toBundle(donation)
        fb.logEvent(FaEvent.BEGIN_CHECKOUT, ecommerceBundle)
    }

    override fun trackPurchase(donation: Donation, orderId: String) {
        trackAchievement("Made Donation")

        val ecommerceBundle = toBundle(donation)
        ecommerceBundle.putString(FaParam.TRANSACTION_ID, orderId)
        ecommerceBundle.putString(FaParam.AFFILIATION, "Entreco - DartsApp")
        ecommerceBundle.putDouble(FaParam.VALUE, formatMicros(donation.priceMicros))
        ecommerceBundle.putString(FaParam.CURRENCY, donation.priceCurrencyCode)

        fb.logEvent(FaEvent.PURCHASE, ecommerceBundle)
    }

    override fun trackPurchaseFailed(productId: String, step: String) {}

    internal fun trackPurchaseFailedBundle(step: String, productId: String): Bundle {
        return Bundle().apply {
                    putString(FaParam.ITEM_CATEGORY, "donations")
                    putString(FaParam.ITEM_ID, productId)
                }
    }

    internal fun toBundle(donation: Donation): Bundle {
        val product = Bundle().apply {
            putString(FaParam.ITEM_ID, donation.sku) // ITEM_ID or ITEM_NAME is required
            putString(FaParam.ITEM_NAME, donation.title)
            putString(FaParam.ITEM_CATEGORY, "Donations")
            putString(FaParam.ITEM_VARIANT, donation.price)
            putString(FaParam.ITEM_BRAND, "Entreco")
            putDouble(FaParam.PRICE, formatMicros(donation.priceMicros))
            putString(FaParam.CURRENCY, donation.priceCurrencyCode) // Item-level currency unused today
            putLong(FaParam.QUANTITY, donation.votes.toLong())
        }

        val items = ArrayList<Bundle>()
        items.add(product)

        val ecommerceBundle = Bundle()
        ecommerceBundle.putParcelableArrayList("items", items)
        return ecommerceBundle
    }

    internal fun formatMicros(priceMicros: Long): Double {
        return try {
            priceMicros.toDouble() / 1000000
        } catch (invalidFormat: NumberFormatException) {
            0.0
        }
    }
}
