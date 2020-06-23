package nl.entreco.dartsscorecard.di.play

import android.content.ComponentName
import android.content.Context
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import dagger.Module
import dagger.Provides
import nl.entreco.dartsscorecard.archive.ArchiveJobService
import nl.entreco.dartsscorecard.archive.ArchiveServiceLauncher
import nl.entreco.dartsscorecard.play.Play01Activity
import nl.entreco.data.billing.PlayBillingRepository
import nl.entreco.data.description.RemoteMatchDescriptionRepository
import nl.entreco.domain.beta.donations.MakePurchaseResponse
import nl.entreco.domain.mastercaller.MusicRepository
import nl.entreco.domain.mastercaller.Sound
import nl.entreco.domain.mastercaller.SoundRepository
import nl.entreco.domain.repository.BillingRepo
import nl.entreco.domain.repository.MatchDescriptionRepository
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
    fun provideSoundRepository(): SoundRepository {
        // TODO Provide original Repo if Dynamic Feature sounds is installed
        return object : SoundRepository {
            override fun play(sound: Sound) {}
            override fun release() {}
        }
    }

    @Provides
    @Play01Scope
    fun provideMusicRepository(): MusicRepository {
        // TODO Provide original Repo if Dynamic Feature sounds is installed
        return object : MusicRepository {
            override fun play() {}
            override fun pause() {}
            override fun resume() {}
            override fun stop() {}
        }
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
