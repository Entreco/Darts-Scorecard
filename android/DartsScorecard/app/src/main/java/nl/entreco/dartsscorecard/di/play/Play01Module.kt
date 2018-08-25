package nl.entreco.dartsscorecard.di.play

import android.content.ComponentName
import android.content.Context
import android.media.SoundPool
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import dagger.Module
import dagger.Provides
import nl.entreco.dartsscorecard.archive.ArchiveJobService
import nl.entreco.dartsscorecard.archive.ArchiveServiceLauncher
import nl.entreco.dartsscorecard.di.application.ApplicationScope
import nl.entreco.dartsscorecard.di.viewmodel.ActivityScope
import nl.entreco.dartsscorecard.play.Play01Activity
import nl.entreco.data.description.RemoteMatchDescriptionRepository
import nl.entreco.data.prefs.SharedAudioPrefRepo
import nl.entreco.data.prefs.SharedRatingPrefRepo
import nl.entreco.data.sound.LocalSoundRepository
import nl.entreco.data.sound.SoundMapper
import nl.entreco.domain.repository.AudioPrefRepository
import nl.entreco.domain.repository.MatchDescriptionRepository
import nl.entreco.domain.repository.RatingPrefRepository
import nl.entreco.domain.repository.SoundRepository

/**
 * Created by Entreco on 14/11/2017.
 */
@Module
class Play01Module(private val activity: Play01Activity) {

    private val prefs = activity.getSharedPreferences("audio", Context.MODE_PRIVATE)

    @Provides
    @Play01Scope
    fun provide01Activity(): Play01Activity {
        return activity
    }

    @Provides
    @Play01Scope
    fun provideSoundMapper(): SoundMapper {
        return SoundMapper()
    }

    @Provides
    @Play01Scope
    fun provideAudioPreferences(): AudioPrefRepository {
        return SharedAudioPrefRepo(prefs)
    }

    @Provides
    @Play01Scope
    fun provideSoundPool(): SoundPool {
        return SoundPool.Builder().setMaxStreams(2).build()
    }

    @Provides
    @Play01Scope
    fun provideSoundRepository(@ActivityScope context: Context, @Play01Scope soundPool: SoundPool, @Play01Scope mapper: SoundMapper, @Play01Scope prefs: AudioPrefRepository): SoundRepository {
        return LocalSoundRepository(context, soundPool, prefs, mapper)
    }

    @Provides
    @Play01Scope
    fun provideArchiveServiceLauncher(@ActivityScope context: Context): ArchiveServiceLauncher {
        return ArchiveServiceLauncher(context, ComponentName(context, ArchiveJobService::class.java))
    }

    @Provides
    @Play01Scope
    fun provideMatchDescriptionRepository(@ApplicationScope config: FirebaseRemoteConfig): MatchDescriptionRepository {
        return RemoteMatchDescriptionRepository(config)
    }
}
