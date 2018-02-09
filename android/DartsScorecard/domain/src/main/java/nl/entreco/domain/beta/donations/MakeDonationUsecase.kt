package nl.entreco.domain.beta.donations

import nl.entreco.domain.BaseUsecase
import nl.entreco.domain.common.executors.Background
import nl.entreco.domain.common.executors.Foreground
import nl.entreco.domain.repository.BillingRepository
import javax.inject.Inject

/**
 * Created by entreco on 09/02/2018.
 */
class MakeDonationUsecase @Inject constructor(private val billingRepository: BillingRepository, bg: Background, fg: Foreground) : BaseUsecase(bg, fg) {

    fun exec(req: MakeDonationRequest, done: (MakeDonationResponse) -> Unit, fail: (Throwable) -> Unit) {
        onBackground({
            val response = billingRepository.donate(req.donation)
            onUi { done(response) }
        }, fail)
    }
}