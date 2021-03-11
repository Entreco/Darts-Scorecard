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
import nl.entreco.dartsscorecard.di.profile.ProfileScope
import nl.entreco.dartsscorecard.play.Play01Activity
import nl.entreco.data.billing.PlayBillingRepository
import nl.entreco.data.description.RemoteMatchDescriptionRepository
import nl.entreco.data.sound.LocalMusicRepository
import nl.entreco.data.sound.LocalSoundRepository
import nl.entreco.data.sound.SoundMapper
import nl.entreco.domain.beta.donations.MakePurchaseResponse
import nl.entreco.domain.repository.AudioPrefRepository
import nl.entreco.domain.repository.BillingRepo
import nl.entreco.domain.repository.MatchDescriptionRepository
import nl.entreco.domain.repository.MusicRepository
import nl.entreco.domain.repository.SoundRepository
import nl.entreco.liblog.Logger
import nl.entreco.shared.scopes.ActivityScope
import nl.entreco.shared.scopes.ApplicationScope
import java.lang.ref.WeakReference

/**
 * Created by Entreco on 14/11/2017.
 */
@Module
class Play01Module(
        private val activity: Play01Activity,
        private val listener: (MakePurchaseResponse) -> Unit
) {

    @Provides
    @Play01Scope
    fun provideBillingService(logger: Logger): BillingRepo {
        return PlayBillingRepository(WeakReference(activity), logger, listener)
    }

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
