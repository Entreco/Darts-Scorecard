package nl.entreco.domain

import nl.entreco.domain.beta.Donation
import nl.entreco.domain.beta.Feature

/**
 * Created by Entreco on 15/11/2017.
 */
interface Analytics {
    fun trackAchievement(achievementId: String)
    fun trackViewFeature(feature: Feature)
    fun trackPurchaseStart(donation: Donation)
    fun trackPurchase(donation: Donation, orderId: String)
    fun trackPurchaseFailed(productId: String, step: String)
    fun trackScore(scored: String, total: Int)
}
