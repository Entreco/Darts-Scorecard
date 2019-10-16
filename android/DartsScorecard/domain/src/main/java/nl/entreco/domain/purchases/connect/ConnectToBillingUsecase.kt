package nl.entreco.domain.purchases.connect

import androidx.annotation.UiThread
import nl.entreco.domain.beta.donations.MakePurchaseResponse
import nl.entreco.domain.repository.BillingRepository
import nl.entreco.shared.BaseUsecase
import nl.entreco.shared.threading.Background
import nl.entreco.shared.threading.Foreground
import javax.inject.Inject

/**
 * Created by entreco on 08/02/2018.
 */
class ConnectToBillingUsecase @Inject constructor(
        private val billingRepository: BillingRepository,
        bg: Background, fg: Foreground
) : BaseUsecase(bg, fg) {

    @UiThread
    fun bind(done: (MakePurchaseResponse) -> Unit) {
        billingRepository.bind(done)
    }

    @UiThread
    fun unbind() {
        billingRepository.unbind()
    }
}