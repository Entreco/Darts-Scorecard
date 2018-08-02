package nl.entreco.dartsscorecard.tv.di.tv

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
}