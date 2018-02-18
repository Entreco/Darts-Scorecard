package nl.entreco.domain.beta.donations

import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import nl.entreco.domain.BaseUsecase
import nl.entreco.domain.common.executors.Background
import nl.entreco.domain.common.executors.Foreground
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
            val result = billingRepository.consume(token)
            onUi { done(ConsumeDonationResponse(result, productId)) }
        }, fail)
    }

    private class ConsumeData {
        @SerializedName("purchaseToken")
        var purchaseToken: String? = null
        @SerializedName("productId")
        var productId: String? = null
    }
}