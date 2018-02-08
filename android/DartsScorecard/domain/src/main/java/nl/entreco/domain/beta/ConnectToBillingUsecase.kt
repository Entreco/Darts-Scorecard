package nl.entreco.domain.beta

import android.support.annotation.UiThread
import nl.entreco.domain.BaseUsecase
import nl.entreco.domain.common.executors.Background
import nl.entreco.domain.common.executors.Foreground
import nl.entreco.domain.repository.BillingRepository
import javax.inject.Inject

/**
 * Created by entreco on 08/02/2018.
 */
class ConnectToBillingUsecase @Inject constructor(private val billingRepository: BillingRepository,
                                                  bg: Background, fg: Foreground) : BaseUsecase(bg, fg) {

    @UiThread
    fun bind(done: (Boolean)->Unit) {
        billingRepository.bind(done)
    }

    @UiThread
    fun unbind() {
        billingRepository.unbind()
    }

    fun exec(done: (List<Donation>)->Unit, fail: (Throwable) -> Unit){
        onBackground({
            val donations = billingRepository.fetchDonations()
            onUi { done(donations) }
        }, fail)
    }
}