package nl.entreco.domain.beta.donations

import nl.entreco.domain.repository.BillingRepository
import nl.entreco.shared.BaseUsecase
import nl.entreco.shared.threading.Background
import nl.entreco.shared.threading.Foreground
import javax.inject.Inject

/**
 * Created by entreco on 09/02/2018.
 */
class FetchDonationsUsecase @Inject constructor(private val billingRepository: BillingRepository, bg: Background, fg: Foreground) : BaseUsecase(bg, fg) {

    companion object{
        private const val PURCHASED = 1
    }

    fun exec(done: (FetchDonationsResponse) -> Unit, fail: (Throwable) -> Unit) {
        onBackground({

            val hasPreviouslyBoughtItems = billingRepository.fetchPurchasedItems().any { it.second == PURCHASED }
            if (hasPreviouslyBoughtItems) {
                billingRepository.fetchDonationsExclAds({ donations ->
                    onUi { done(FetchDonationsResponse.Ok(donations.sortedBy { it.priceMicros }, true)) }
                }) {
                    onUi { done(FetchDonationsResponse.Error(it, true)) }
                }

            } else {
                billingRepository.fetchDonationsInclAds({ donations ->
                    onUi { done(FetchDonationsResponse.Ok(donations.sortedBy { it.priceMicros }, false)) }
                }) {
                    onUi { done(FetchDonationsResponse.Error(it, false)) }
                }
            }
        }, fail)
    }
}