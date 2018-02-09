package nl.entreco.domain.beta.donations

import android.util.Log
import nl.entreco.domain.BaseUsecase
import nl.entreco.domain.common.executors.Background
import nl.entreco.domain.common.executors.Foreground
import nl.entreco.domain.repository.BillingRepository
import org.json.JSONObject
import javax.inject.Inject

/**
 * Created by entreco on 09/02/2018.
 */
class ConsumeDonationUsecase @Inject constructor(private val billingRepository: BillingRepository, bg: Background, fg: Foreground) : BaseUsecase(bg, fg) {

    fun exec(req: ConsumeDonationRequest, done:(ConsumeDonationResponse) -> Unit, fail: (Throwable) -> Unit){
        onBackground({
            val json = JSONObject(req.purchaseData)
            val token = json.getString("purchaseToken")
            val productId = json.getString("productId")
            val result = billingRepository.consume(token)
            Log.w("DONATE", "exec usecase: $json $result")
            onUi { done(ConsumeDonationResponse(result, productId)) }
        }, fail)
    }
}