package nl.entreco.dartsscorecard.di.viewmodel

import android.arch.lifecycle.Lifecycle
import android.content.Context
import android.content.res.Resources
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import dagger.Module
import dagger.Provides
import nl.entreco.data.billing.BillingServiceConnection
import nl.entreco.data.billing.PlayStoreBillingRepository
import nl.entreco.domain.repository.BillingRepository

/**
 * Created by Entreco on 14/11/2017.
 */
@Module
class ViewModelModule(private val activity: FragmentActivity) {
    @Provides
    @ActivityScope
    fun context(): Context = activity

    @Provides
    @ActivityScope
    fun resources(): Resources = activity.resources

    @Provides
    @ActivityScope
    fun lifeCycle(): Lifecycle {
        return activity.lifecycle
    }

    @Provides
    @ActivityScope
    fun provideServiceConnection(): BillingServiceConnection {
        return BillingServiceConnection()
    }

    @Provides
    @ActivityScope
    fun provideBillingRepository(@ActivityScope context: Context, @ActivityScope serviceConnection: BillingServiceConnection): BillingRepository {
        return PlayStoreBillingRepository(context, serviceConnection)
    }
}