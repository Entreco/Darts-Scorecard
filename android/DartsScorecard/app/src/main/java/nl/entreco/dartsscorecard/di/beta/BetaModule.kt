package nl.entreco.dartsscorecard.di.beta

import android.app.Activity
import dagger.Module
import dagger.Provides
import nl.entreco.data.billing.PlayBillingRepository
import nl.entreco.domain.beta.donations.MakePurchaseResponse
import nl.entreco.domain.repository.BillingRepo
import nl.entreco.liblog.Logger
import java.lang.ref.WeakReference

/**
 * Created by entreco on 30/01/2018.
 */
@Module
class BetaModule(
        private val activity: Activity,
        private val listener: (MakePurchaseResponse) -> Unit
) {

    @Provides
    @BetaScope
    fun provideBillingService(logger: Logger): BillingRepo {
        return PlayBillingRepository(WeakReference(activity), logger, listener)
    }
}