package nl.entreco.dartsscorecard.tv.di.tv

import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import nl.entreco.dartsscorecard.tv.TvApp
import nl.entreco.dartsscorecard.tv.TvLogger
import nl.entreco.shared.log.Logger

@Module
class TvModule(@TvScope private val tvApp: TvApp) {

    @Provides
    @TvScope
    fun provideTvApp(): TvApp {
        return tvApp
    }

    @Provides
    @TvScope
    fun provideLogger(): Logger {
        return TvLogger("DscTv")
    }

    @Provides
    @TvScope
    fun provideSecondaryFirebaseOptions() : FirebaseOptions {
        return FirebaseOptions.Builder()
                .setApplicationId("1:920806666928:android:a6464be80f55030c")
                .setApiKey("AIzaSyAoEjAhIk5DU5BDdo-2MGeJ0qUzkxYJkqM")
                .build()
    }

    @Provides
    @TvScope
    fun provideFireStore(@TvScope app: TvApp, @TvScope options: FirebaseOptions): FirebaseFirestore {
        FirebaseApp.initializeApp(app, options, "tv")
        return FirebaseFirestore.getInstance()
    }
}