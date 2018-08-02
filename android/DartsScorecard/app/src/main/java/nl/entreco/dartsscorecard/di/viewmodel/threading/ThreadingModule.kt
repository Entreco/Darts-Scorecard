package nl.entreco.dartsscorecard.di.viewmodel.threading

import android.os.Handler
import android.os.Looper
import dagger.Module
import dagger.Provides
import nl.entreco.shared.threading.Background
import nl.entreco.shared.threading.BgExecutor
import nl.entreco.shared.threading.FgExecutor
import nl.entreco.shared.threading.Foreground

/**
 * Created by Entreco on 17/12/2017.
 */
@Module
class ThreadingModule {
    @Provides
    fun provideBackground(): Background {
        return BgExecutor()
    }

    @Provides
    fun provideForeground(handler: Handler): Foreground {
        return FgExecutor(handler)
    }

    @Provides
    fun provideHandler(): Handler {
        return Handler(Looper.myLooper())
    }
}
