package nl.entreco.dartsscorecard.di.launch

import android.app.Activity
import dagger.Module
import dagger.Provides
import nl.entreco.data.billing.PlayBillingRepository
import nl.entreco.domain.beta.donations.MakePurchaseResponse
import nl.entreco.domain.repository.BillingRepo
import nl.entreco.liblog.Logger
import java.lang.ref.WeakReference

/**
 * Created by Entreco on 12/12/2017.
 */
@Module
class LaunchModule(
        private val activity: Activity,
        private val listener: (MakePurchaseResponse) -> Unit
) {

    @Provides
    @LaunchScope
    fun provideBillingService(logger: Logger): BillingRepo {
        return PlayBillingRepository(WeakReference(activity), logger, listener)
    }
}