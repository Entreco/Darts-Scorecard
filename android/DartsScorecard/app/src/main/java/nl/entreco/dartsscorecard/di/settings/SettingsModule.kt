package nl.entreco.dartsscorecard.di.settings

import android.app.Activity
import dagger.Module
import dagger.Provides
import nl.entreco.dartsscorecard.beta.donate.DonateCallback
import nl.entreco.dartsscorecard.di.beta.BetaScope
import nl.entreco.data.billing.PlayBillingRepository
import nl.entreco.domain.beta.donations.MakePurchaseResponse
import nl.entreco.domain.repository.BillingRepo

@Module
class SettingsModule(
        private val activity: Activity,
        private val listener: (MakePurchaseResponse)->Unit) {

    @Provides
    @SettingsScope
    fun provideBillingService(): BillingRepo {
        return PlayBillingRepository(activity, listener)
    }

}