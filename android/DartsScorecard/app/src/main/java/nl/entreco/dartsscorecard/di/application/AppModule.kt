package nl.entreco.dartsscorecard.di.application

import android.arch.persistence.room.Room
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import dagger.Module
import dagger.Provides
import nl.entreco.dartsscorecard.App
import nl.entreco.dartsscorecard.BuildConfig
import nl.entreco.dartsscorecard.DscLogger
import nl.entreco.data.analytics.FirebaseAnalytics
import nl.entreco.data.db.DscDatabase
import nl.entreco.domain.Analytics
import nl.entreco.domain.common.log.Logger
import javax.inject.Named
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import nl.entreco.dartsscorecard.R


/**
 * Created by Entreco on 14/11/2017.
 */
@Module
class AppModule(val app: App) {

    @Provides
    @ApplicationScope
    fun application() = app

    @Provides
    @ApplicationScope
    fun provideAnalytics(): Analytics {
        return FirebaseAnalytics(app.applicationContext)
    }

    @Provides
    @ApplicationScope
    fun provideLogger(): Logger {
        return DscLogger("Dsc")
    }

    @Provides
    @ApplicationScope
    fun provideDb(app: App): DscDatabase {
        return Room.databaseBuilder(app, DscDatabase::class.java, DscDatabase.name)
                .fallbackToDestructiveMigration()
                .build()
    }

    @Provides
    @ApplicationScope
    fun provideFireStore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @ApplicationScope
    fun provideRemoteConfig(): FirebaseRemoteConfig {
        return FirebaseRemoteConfig.getInstance().apply {
            val configSettings = FirebaseRemoteConfigSettings.Builder()
                    .setDeveloperModeEnabled(BuildConfig.DEBUG)
                    .build()
            setConfigSettings(configSettings)
            setDefaults(R.xml.remote_config_defaults)
        }
    }

    @Provides
    @Named("debugMode")
    @ApplicationScope
    fun provideDebugMode(): Boolean {
        return BuildConfig.DEBUG
    }
}
