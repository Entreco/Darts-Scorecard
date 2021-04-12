package nl.entreco.dartsscorecard.di.play

import android.content.ComponentName
import android.content.Context
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import dagger.Module
import dagger.Provides
import nl.entreco.dartsscorecard.archive.ArchiveJobService
import nl.entreco.dartsscorecard.archive.ArchiveServiceLauncher
import nl.entreco.dartsscorecard.dynamic.DynamicInstaller
import nl.entreco.dartsscorecard.dynamic.Installer
import nl.entreco.dartsscorecard.dynamic.NoInstaller
import nl.entreco.dartsscorecard.dynamic.NoMusicRepository
import nl.entreco.dartsscorecard.dynamic.NoSoundRepository
import nl.entreco.dartsscorecard.dynamic.SoundModuleProvider
import nl.entreco.dartsscorecard.play.Play01Activity
import nl.entreco.data.billing.PlayBillingRepository
import nl.entreco.data.description.RemoteMatchDescriptionRepository
import nl.entreco.domain.beta.donations.MakePurchaseResponse
import nl.entreco.domain.mastercaller.MusicRepository
import nl.entreco.domain.mastercaller.SoundRepository
import nl.entreco.domain.repository.AudioPrefRepository
import nl.entreco.domain.repository.BillingRepo
import nl.entreco.domain.repository.MatchDescriptionRepository
import nl.entreco.liblog.Logger
import nl.entreco.shared.scopes.ActivityScope
import nl.entreco.shared.scopes.ApplicationScope
import java.lang.ref.WeakReference

private const val DYNAMIC_PROVIDER = "nl.entreco.dartsscorecard.sounds.DynamicSoundProvider"
private const val SOUNDS = "sounds"

/**
 * Created by Entreco on 14/11/2017.
 */
@Module
class Play01Module(
        private val activity: Play01Activity,
        private val listener: (MakePurchaseResponse) -> Unit,
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
    fun provideSplitInstallManager(@ActivityScope context: Context): SplitInstallManager {
        return SplitInstallManagerFactory.create(context)
    }

    @Provides
    @Play01Scope
    fun provideSplitInstallRequest() = SplitInstallRequest.newBuilder()
            .addModule(SOUNDS)
            .build()

    @Provides
    @Play01Scope
    fun provideSoundRepository(
            @ActivityScope context: Context,
            splitInstallManager: SplitInstallManager,
            prefs: AudioPrefRepository,
    ): SoundRepository {
        return if (splitInstallManager.installedModules.contains(SOUNDS)) {
            val provider = Class.forName(DYNAMIC_PROVIDER).kotlin.objectInstance as? SoundModuleProvider
            provider?.provideSoundRepository(context.applicationContext, prefs) ?: NoSoundRepository()
        } else NoSoundRepository()
    }

    @Provides
    @Play01Scope
    fun provideMusicRepository(
            @ActivityScope context: Context,
            splitInstallManager: SplitInstallManager,
    ): MusicRepository {
        return if (splitInstallManager.installedModules.contains(SOUNDS)) {
            val provider = Class.forName(DYNAMIC_PROVIDER).kotlin.objectInstance as? SoundModuleProvider
            provider?.provideMusicRepository(context.applicationContext) ?: NoMusicRepository()
        } else NoMusicRepository()
    }

    @Provides
    @Play01Scope
    fun provideDynamicInstaller(
            splitInstallRequest: SplitInstallRequest,
            splitInstallManager: SplitInstallManager,
    ): Installer {
        return if (splitInstallManager.installedModules.contains(SOUNDS)) NoInstaller()
        else DynamicInstaller(splitInstallRequest, splitInstallManager)
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
