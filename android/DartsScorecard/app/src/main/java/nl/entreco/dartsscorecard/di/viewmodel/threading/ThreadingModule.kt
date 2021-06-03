package nl.entreco.dartsscorecard.di.viewmodel.threading

import android.os.Handler
import android.os.Looper
import dagger.Module
import dagger.Provides
import nl.entreco.libcore.threading.Background
import nl.entreco.libcore.threading.BgExecutor
import nl.entreco.libcore.threading.FgExecutor
import nl.entreco.libcore.threading.Foreground

/**
 * Created by Entreco on 17/12/2017.
 */
@Module
class ThreadingModule {
    @Provides
    fun provideBackground(): nl.entreco.libcore.threading.Background {
        return nl.entreco.libcore.threading.BgExecutor()
    }

    @Provides
    fun provideForeground(handler: Handler): nl.entreco.libcore.threading.Foreground {
        return nl.entreco.libcore.threading.FgExecutor(handler)
    }

    @Provides
    fun provideHandler(): Handler {
        return Handler(Looper.getMainLooper())
    }
}
