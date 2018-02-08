package nl.entreco.dartsscorecard.di.beta

import android.arch.lifecycle.Lifecycle
import android.content.Context
import dagger.Module
import dagger.Provides
import nl.entreco.dartsscorecard.di.viewmodel.ActivityScope
import nl.entreco.data.billing.BillingServiceConnection
import nl.entreco.data.billing.PlayStoreBillingRepository
import nl.entreco.domain.repository.BillingRepository

/**
 * Created by entreco on 30/01/2018.
 */
@Module
class BetaModule(private val lifeCycle: Lifecycle) {

    @Provides
    @BetaScope
    fun provideLifeCycle(): Lifecycle {
        return lifeCycle
    }

    @Provides
    @BetaScope
    fun provideServiceConnection() : BillingServiceConnection {
        return BillingServiceConnection()
    }

    @Provides
    @BetaScope
    fun provideBillingRepository(@ActivityScope context: Context, serviceConnection: BillingServiceConnection): BillingRepository {
        return PlayStoreBillingRepository(context, serviceConnection)
    }
}