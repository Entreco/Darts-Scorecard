package nl.entreco.dartsscorecard.di.settings

import android.app.Activity
import dagger.Module
import dagger.Provides
import nl.entreco.data.billing.PlayBillingRepository
import nl.entreco.domain.beta.donations.MakePurchaseResponse
import nl.entreco.domain.repository.BillingRepo
import nl.entreco.liblog.Logger
import java.lang.ref.WeakReference

@Module
class SettingsModule(
        private val activity: Activity,
        private val listener: (MakePurchaseResponse) -> Unit) {

    @Provides
    @SettingsScope
    fun provideBillingService(logger: Logger): BillingRepo {
        return PlayBillingRepository(WeakReference(activity), logger, listener)
    }

}