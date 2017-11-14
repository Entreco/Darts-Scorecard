package nl.entreco.dartsscorecard.di.application

import dagger.Module
import dagger.Provides
import nl.entreco.dartsscorecard.App
import javax.inject.Singleton

/**
 * Created by Entreco on 14/11/2017.
 */
@Module class AppModule(val app: App) {
    @Provides @Singleton fun provideApp() = app
}