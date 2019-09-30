package nl.entreco.dartsscorecard.di.viewmodel

import android.content.Context
import android.content.res.Resources
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import dagger.Module
import dagger.Provides
import nl.entreco.data.billing.GooglePlayConnection
import nl.entreco.data.billing.PlayStoreBillingRepository
import nl.entreco.data.prefs.SharedAudioPrefRepo
import nl.entreco.data.prefs.SharedRatingPrefRepo
import nl.entreco.domain.repository.AudioPrefRepository
import nl.entreco.domain.repository.BillingRepository
import nl.entreco.domain.repository.RatingPrefRepository
import nl.entreco.shared.scopes.ActivityScope

/**
 * Created by Entreco on 14/11/2017.
 */
@Module
class ViewModelModule(private val activity: FragmentActivity) {

    private val prefsRating = activity.getSharedPreferences("rating", Context.MODE_PRIVATE)
    private val prefsAudio = activity.getSharedPreferences("audio", Context.MODE_PRIVATE)

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
        return SharedRatingPrefRepo(prefsRating)
    }

    @Provides
    @ActivityScope
    fun provideAudioPreferences(): AudioPrefRepository {
        return SharedAudioPrefRepo(prefsAudio)
    }

    @Provides
    @ActivityScope
    fun provideAlertDialogBuilder(@ActivityScope context: Context): AlertDialog.Builder {
        return AlertDialog.Builder(context)
    }

    @Provides
    @ActivityScope
    fun provideBillingRepository(): BillingRepository {
        val playConnection = GooglePlayConnection()
        return PlayStoreBillingRepository(activity, playConnection)
    }
}