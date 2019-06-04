package nl.entreco.dartsscorecard.di.viewmodel

import androidx.lifecycle.Lifecycle
import android.content.Context
import android.content.res.Resources
import androidx.fragment.app.FragmentManager
import androidx.appcompat.app.AlertDialog
import dagger.Module
import dagger.Provides
import nl.entreco.data.billing.BillingServiceConnection
import nl.entreco.data.billing.PlayStoreBillingRepository
import nl.entreco.data.prefs.SharedRatingPrefRepo
import nl.entreco.domain.repository.BillingRepository
import nl.entreco.domain.repository.RatingPrefRepository
import nl.entreco.shared.scopes.ActivityScope

/**
 * Created by Entreco on 14/11/2017.
 */
@Module
class ViewModelModule(private val activity: androidx.fragment.app.FragmentActivity) {

    private val prefs = activity.getSharedPreferences("rating", Context.MODE_PRIVATE)

    @Provides
    @ActivityScope
    fun context(): Context = activity

    @Provides
    @ActivityScope
    fun fragmentManager(): FragmentManager = activity.supportFragmentManager

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
    fun provideRatingPreferences(): RatingPrefRepository {
        return SharedRatingPrefRepo(prefs)
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
}