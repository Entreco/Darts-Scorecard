package nl.entreco.domain.beta.donations

import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import nl.entreco.domain.BaseUsecase
import nl.entreco.shared.threading.Background
import nl.entreco.shared.threading.Foreground
import nl.entreco.domain.repository.BillingRepository
import javax.inject.Inject

/**
 * Created by entreco on 09/02/2018.
 */
class ConsumeDonationUsecase @Inject constructor(private val billingRepository: BillingRepository,
                                                 bg: Background,
                                                 fg: Foreground) : BaseUsecase(bg, fg) {

    private val gson by lazy { GsonBuilder().create() }

    fun exec(req: ConsumeDonationRequest, done: (ConsumeDonationResponse) -> Unit, fail: (Throwable) -> Unit) {
        onBackground({

            val json = gson.fromJson(req.purchaseData, ConsumeData::class.java)
            val token = json.purchaseToken!!
            val productId = json.productId!!
            val orderId = json.orderId!!

            val result = if(req.requiresConsumption) billingRepository.consume(token) else ConsumeDonationResponse.CONSUME_OK
            onUi { done(ConsumeDonationResponse(result, productId, orderId)) }
        }, fail)
    }

    private class ConsumeData {
        @SerializedName("purchaseToken")
        var purchaseToken: String? = null
        @SerializedName("productId")
        var productId: String? = null
        @SerializedName("orderId")
        var orderId: String? = null
    }
}
