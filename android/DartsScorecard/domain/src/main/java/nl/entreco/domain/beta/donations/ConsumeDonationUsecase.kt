package nl.entreco.domain.beta.donations

import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import nl.entreco.domain.repository.BillingRepository
import nl.entreco.shared.BaseUsecase
import nl.entreco.shared.threading.Background
import nl.entreco.shared.threading.Foreground
import javax.inject.Inject

/**
 * Created by entreco on 09/02/2018.
 */
class ConsumeDonationUsecase @Inject constructor(
        private val billingRepository: BillingRepository,
        bg: Background,
        fg: Foreground
) : BaseUsecase(bg, fg) {

    fun exec(req: ConsumeDonationRequest, done: (ConsumeDonationResponse) -> Unit, fail: (Throwable) -> Unit) {
        onBackground({

            val token = req.purchaseToken
            val productId = req.productId
            val orderId = req.orderId

            if (req.requiresConsumption) {
                billingRepository.consume(token) {
                    onUi { done(ConsumeDonationResponse(it, productId, orderId)) }
                }
            } else {
                billingRepository.acknowledge(token){
                    onUi { done(ConsumeDonationResponse(ConsumeDonationResponse.CONSUME_OK, productId, orderId)) }
                }
            }
        }, fail)
    }
}
