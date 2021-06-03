package nl.entreco.dartsscorecard.di.viewmodel

import android.content.Context
import android.content.res.Resources
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import dagger.Module
import dagger.Provides
import nl.entreco.data.prefs.SharedAudioPrefRepo
import nl.entreco.data.prefs.SharedBotPrefRepo
import nl.entreco.data.prefs.SharedRatingPrefRepo
import nl.entreco.domain.repository.AudioPrefRepository
import nl.entreco.domain.repository.BotPrefRepository
import nl.entreco.domain.repository.RatingPrefRepository
import nl.entreco.libcore.scopes.ActivityScope

/**
 * Created by Entreco on 14/11/2017.
 */
@Module
class ViewModelModule(private val activity: FragmentActivity) {

    private val prefsRating = activity.getSharedPreferences("rating", Context.MODE_PRIVATE)
    private val prefsAudio = activity.getSharedPreferences("audio", Context.MODE_PRIVATE)
    private val prefsBot = activity.getSharedPreferences("bot", Context.MODE_PRIVATE)

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
    fun provideBotPreferences(): BotPrefRepository {
        return SharedBotPrefRepo(prefsBot)
    }

    @Provides
    @ActivityScope
    fun provideAlertDialogBuilder(@ActivityScope context: Context): AlertDialog.Builder {
        return AlertDialog.Builder(context)
    }
}