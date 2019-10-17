package nl.entreco.dartsscorecard.di.launch

import android.app.Activity
import dagger.Module
import dagger.Provides
import nl.entreco.dartsscorecard.di.beta.BetaScope
import nl.entreco.data.billing.PlayBillingRepository
import nl.entreco.domain.beta.donations.MakePurchaseResponse
import nl.entreco.domain.repository.BillingRepo

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
    fun provideBillingService(): BillingRepo {
        return PlayBillingRepository(activity, listener)
    }
}