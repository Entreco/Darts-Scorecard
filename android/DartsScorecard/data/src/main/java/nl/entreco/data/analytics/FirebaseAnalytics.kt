package nl.entreco.data.analytics

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import nl.entreco.domain.Analytics
import nl.entreco.domain.beta.Donation

/**
 * Created by Entreco on 15/11/2017.
 */
class FirebaseAnalytics(context: Context) : Analytics {

    private val fb by lazy { FirebaseAnalytics.getInstance(context) }

    override fun trackAchievement(achievementId: String) {
        fb.logEvent(FirebaseAnalytics.Event.UNLOCK_ACHIEVEMENT, Bundle().apply {
            putString(FirebaseAnalytics.Param.ACHIEVEMENT_ID, achievementId)
        })
    }

    override fun trackViewDonations() {
        fb.logEvent(FirebaseAnalytics.Event.VIEW_ITEM_LIST, Bundle().apply {
            putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "donations")
        })
    }

    override fun trackPurchase(donation: Donation) {
        trackAchievement("Made Donation")
        fb.logEvent(FirebaseAnalytics.Event.ECOMMERCE_PURCHASE, Bundle()
                .apply {
                    putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "donations")
                    putString(FirebaseAnalytics.Param.PRICE, donation.price)
                    putString(FirebaseAnalytics.Param.ITEM_ID, donation.sku)
                })
    }

    override fun trackPurchaseFailed(productId: String, step: String) {
        fb.logEvent(FirebaseAnalytics.Event.CHECKOUT_PROGRESS, Bundle()
                .apply {
                    putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "donations")
                    putString(FirebaseAnalytics.Param.CHECKOUT_STEP, step)
                    putString(FirebaseAnalytics.Param.ITEM_ID, productId)
                })
    }
}
