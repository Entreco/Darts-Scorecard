package nl.entreco.dartsscorecard.di.viewmodel.threading

import android.os.Handler
import dagger.Module
import dagger.Provides
import nl.entreco.dartsscorecard.di.viewmodel.ActivityScope
import nl.entreco.domain.common.executors.Background
import nl.entreco.domain.common.executors.BgExecutor
import nl.entreco.domain.common.executors.FgExecutor
import nl.entreco.domain.common.executors.Foreground

/**
 * Created by Entreco on 17/12/2017.
 */
@Module
class ThreadingModule {
    @Provides
    @ActivityScope
    fun provideBackground(): Background {
        return BgExecutor()
    }

    @Provides
    @ActivityScope
    fun provideForeground(handler: Handler): Foreground {
        return FgExecutor(handler)
    }

    @Provides
    @ActivityScope
    fun provideHandler(): Handler {
        return Handler()
    }
}