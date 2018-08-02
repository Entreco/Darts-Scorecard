package nl.entreco.dartsscorecard.tv.di.viewmodel.threading

import android.os.Handler
import android.os.Looper
import dagger.Module
import dagger.Provides
import nl.entreco.dartsscorecard.tv.di.viewmodel.TvActivityScope
import nl.entreco.shared.threading.Background
import nl.entreco.shared.threading.BgExecutor
import nl.entreco.shared.threading.FgExecutor
import nl.entreco.shared.threading.Foreground

@Module
class TvThreadingModule {
    @Provides
    @TvActivityScope
    fun provideBackground(): Background {
        return BgExecutor()
    }

    @Provides
    @TvActivityScope
    fun provideForeground(handler: Handler): Foreground {
        return FgExecutor(handler)
    }

    @Provides
    @TvActivityScope
    fun provideHandler(): Handler {
        return Handler(Looper.myLooper())
    }
}