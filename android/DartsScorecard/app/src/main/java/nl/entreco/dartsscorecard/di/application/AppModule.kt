package nl.entreco.dartsscorecard.di.application

import dagger.Module
import dagger.Provides
import nl.entreco.dartsscorecard.App
import nl.entreco.dartsscorecard.analytics.Analytics
import nl.entreco.dartsscorecard.analytics.FirebaseAnalytics

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
}