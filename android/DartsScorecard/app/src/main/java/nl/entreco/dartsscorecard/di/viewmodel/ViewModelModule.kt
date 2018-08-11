package nl.entreco.dartsscorecard.di.viewmodel

import android.arch.lifecycle.Lifecycle
import android.content.Context
import android.content.res.Resources
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AlertDialog
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import nl.entreco.dartsscorecard.di.application.ApplicationScope
import nl.entreco.dartsscorecard.di.play.Play01Scope
import nl.entreco.data.billing.BillingServiceConnection
import nl.entreco.data.billing.PlayStoreBillingRepository
import nl.entreco.data.stream.FirebaseIceRepository
import nl.entreco.data.stream.FirebaseSignallingRepository
import nl.entreco.domain.repository.BillingRepository
import nl.entreco.domain.repository.IceRepository
import nl.entreco.domain.repository.SignallingRepository
import nl.entreco.shared.log.Logger
import javax.inject.Named

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
    fun provideAlertDialogBuilder(@ActivityScope context: Context): AlertDialog.Builder {
        return AlertDialog.Builder(context)
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

    @Provides
    @ActivityScope
    fun provideSignallingRepository(@ApplicationScope db: FirebaseDatabase,
                                    @ApplicationScope logger: Logger,
                                    @ApplicationScope @Named("uuid") uuid: String): SignallingRepository {
        return FirebaseSignallingRepository(db, logger, uuid)
    }
}