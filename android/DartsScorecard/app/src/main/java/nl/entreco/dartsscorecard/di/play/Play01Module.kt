package nl.entreco.dartsscorecard.di.play

import android.content.ComponentName
import android.content.Context
import android.media.MediaPlayer
import android.media.SoundPool
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import dagger.Module
import dagger.Provides
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.archive.ArchiveJobService
import nl.entreco.dartsscorecard.archive.ArchiveServiceLauncher
import nl.entreco.shared.scopes.ApplicationScope
import nl.entreco.shared.scopes.ActivityScope
import nl.entreco.dartsscorecard.play.Play01Activity
import nl.entreco.data.description.RemoteMatchDescriptionRepository
import nl.entreco.data.prefs.SharedBotPrefRepo
import nl.entreco.data.sound.LocalMusicRepository
import nl.entreco.data.sound.LocalSoundRepository
import nl.entreco.data.sound.SoundMapper
import nl.entreco.domain.repository.*

/**
 * Created by Entreco on 14/11/2017.
 */
@Module
class Play01Module(private val activity: Play01Activity) {

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
    fun provideSoundRepository(@ActivityScope context: Context, @Play01Scope mapper: SoundMapper, @Play01Scope prefs: AudioPrefRepository): SoundRepository {
        val soundPool = SoundPool.Builder().setMaxStreams(2).build()
        return LocalSoundRepository(context, soundPool, prefs, mapper)
    }

    @Provides
    @Play01Scope
    fun provideMusicRepository(@ActivityScope context: Context): MusicRepository {
        val mediaPlayer = MediaPlayer.create(context, R.raw.pdc_tune)
        mediaPlayer.isLooping = true
        return LocalMusicRepository(mediaPlayer)
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
