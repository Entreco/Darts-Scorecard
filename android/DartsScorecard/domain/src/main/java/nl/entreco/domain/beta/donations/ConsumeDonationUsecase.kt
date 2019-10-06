package nl.entreco.domain.beta.donations

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
            val sku = req.sku
            val orderId = req.orderId

            if (req.requiresConsumption) {
                billingRepository.consume(token) { response ->
                    val result = when (response) {
                        is MakePurchaseResponse.Consumed -> ConsumeDonationResponse.Success(sku, orderId)
                        is MakePurchaseResponse.Error    -> ConsumeDonationResponse.Error(sku, response.code)
                        else                             -> ConsumeDonationResponse.Error(sku, -180)
                    }
                    onUi { done(result) }
                }
            } else {
                billingRepository.acknowledge(token) { response ->
                    val result = when (response) {
                        is MakePurchaseResponse.Acknowledged -> ConsumeDonationResponse.Success(sku, orderId)
                        is MakePurchaseResponse.Error        -> ConsumeDonationResponse.Error(sku, response.code)
                        else                                 -> ConsumeDonationResponse.Error(sku, -180)
                    }
                    onUi { done(result) }
                }
            }
        }, fail)
    }
}
