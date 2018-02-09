package nl.entreco.domain

import nl.entreco.domain.beta.Donation

/**
 * Created by Entreco on 15/11/2017.
 */
interface Analytics {
    fun trackAchievement(achievementId: String)
    fun trackViewDonations()
    fun trackPurchase(donation: Donation)
    fun trackPurchaseFailed(productId: String, step: String)
}