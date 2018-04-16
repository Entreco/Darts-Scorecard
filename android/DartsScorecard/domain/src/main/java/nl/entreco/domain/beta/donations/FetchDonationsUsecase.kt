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

    fun exec(done: (FetchDonationsResponse)->Unit, fail: (Throwable) -> Unit){
        onBackground({
            val donations = billingRepository.fetchDonations().sortedBy { it.priceMicros.toLong() }
            val sorted = donations.sortedBy { it.price }
            onUi { done(FetchDonationsResponse(sorted)) }
        }, fail)
    }
}