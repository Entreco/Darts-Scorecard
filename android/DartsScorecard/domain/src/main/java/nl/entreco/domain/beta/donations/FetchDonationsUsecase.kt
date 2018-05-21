package nl.entreco.domain.beta.donations

import nl.entreco.domain.BaseUsecase
import nl.entreco.domain.common.executors.Background
import nl.entreco.domain.common.executors.Foreground
import nl.entreco.domain.repository.BillingRepository
import javax.inject.Inject

/**
 * Created by entreco on 09/02/2018.
 */
class FetchDonationsUsecase @Inject constructor(private val billingRepository: BillingRepository, bg: Background, fg: Foreground) : BaseUsecase(bg, fg) {

    fun exec(done: (FetchDonationsResponse) -> Unit, fail: (Throwable) -> Unit) {
        onBackground({

            val hasPreviouslyBoughtItems = billingRepository.fetchPurchasedItems().isNotEmpty()
            if (hasPreviouslyBoughtItems) {
                val donations = billingRepository.fetchDonationsExclAds().sortedBy { it.priceMicros.toLong() }
                onUi { done(FetchDonationsResponse(donations, true)) }
            } else {
                val donations = billingRepository.fetchDonationsInclAds().sortedBy { it.priceMicros.toLong() }
                onUi { done(FetchDonationsResponse(donations, false)) }
            }
        }, fail)
    }
}