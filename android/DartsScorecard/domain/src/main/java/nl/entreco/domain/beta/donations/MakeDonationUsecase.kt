package nl.entreco.domain.beta.donations

import nl.entreco.domain.repository.BillingRepository
import nl.entreco.shared.BaseUsecase
import nl.entreco.shared.threading.Background
import nl.entreco.shared.threading.Foreground
import javax.inject.Inject

/**
 * Created by entreco on 09/02/2018.
 */
class MakeDonationUsecase @Inject constructor(private val billingRepository: BillingRepository, bg: Background, fg: Foreground) : BaseUsecase(bg, fg) {

    fun exec(req: MakeDonationRequest, done: (MakePurchaseResponse) -> Unit, fail: (Throwable) -> Unit) {
        onBackground({
            billingRepository.donate(req.donation) {
                onUi { done(it) }
            }
        }, fail)
    }
}