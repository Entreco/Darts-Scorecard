package nl.entreco.dartsscorecard.di.application

import android.arch.persistence.room.Room
import dagger.Module
import dagger.Provides
import nl.entreco.dartsscorecard.App
import nl.entreco.dartsscorecard.DscLogger
import nl.entreco.data.analytics.FirebaseAnalytics
import nl.entreco.data.db.DscDatabase
import nl.entreco.domain.Analytics
import nl.entreco.domain.Logger

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
    fun provideLogger(): Logger {
        return DscLogger("Dsc")
    }

    @Provides
    fun provideDb(app: App): DscDatabase {
        return Room.databaseBuilder(app, DscDatabase::class.java, DscDatabase.name).build()
    }
}