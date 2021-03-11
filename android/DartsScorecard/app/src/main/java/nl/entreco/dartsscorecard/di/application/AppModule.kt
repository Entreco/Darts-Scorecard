package nl.entreco.dartsscorecard.di.application

import android.content.Context
import androidx.room.Room
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import dagger.Module
import dagger.Provides
import nl.entreco.dartsscorecard.App
import nl.entreco.dartsscorecard.BuildConfig
import nl.entreco.dartsscorecard.R
import nl.entreco.data.analytics.FirebaseAnalytics
import nl.entreco.data.db.DscDatabase
import nl.entreco.domain.Analytics
import nl.entreco.liblog.Logger
import nl.entreco.shared.scopes.AppContext
import nl.entreco.shared.scopes.ApplicationScope
import java.util.UUID
import javax.inject.Named


/**
 * Created by Entreco on 14/11/2017.
 */
@Module
class AppModule(val app: App) {

    @Provides
    @ApplicationScope
    fun application() = app

    @Provides
    @AppContext
    @ApplicationScope
    fun applicationContext(): Context = app.applicationContext

    @Provides
    @ApplicationScope
    fun provideAnalytics(): Analytics {
        return FirebaseAnalytics(app.applicationContext)
    }

    @Provides
    @ApplicationScope
    fun provideLogger(): Logger {
        return Logger.default("Dsc")
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
    fun provideFirebaseDatabase(): FirebaseDatabase {
        return FirebaseDatabase.getInstance()
    }

    @Provides
    @ApplicationScope
    @Named("uuid")
    fun provideUuid(): String {
        return UUID.randomUUID().toString()
    }

    @Provides
    @ApplicationScope
    fun provideRemoteConfig(): FirebaseRemoteConfig {
        return FirebaseRemoteConfig.getInstance().apply {
            val configSettings = FirebaseRemoteConfigSettings.Builder().build()
            setConfigSettingsAsync(configSettings)
            setDefaultsAsync(R.xml.remote_config_defaults)
        }
    }

    @Provides
    @Named("debugMode")
    @ApplicationScope
    fun provideDebugMode(): Boolean {
        return BuildConfig.DEBUG
    }
}
