package nl.entreco.dartsscorecard.play

import dagger.Module
import dagger.Provides
import nl.entreco.dartsscorecard.di.viewmodel.ActivityScope
import nl.entreco.data.DscDatabase
import nl.entreco.data.play.repository.LocalGameRepository
import nl.entreco.domain.executors.Background
import nl.entreco.domain.executors.BgExecutor
import nl.entreco.domain.executors.FgExecutor
import nl.entreco.domain.executors.Foreground
import nl.entreco.domain.play.repository.GameRepository

/**
 * Created by Entreco on 14/11/2017.
 */
@Module
class Play01Module {

    @Provides
    @ActivityScope
    fun provideGameRepository(db: DscDatabase): GameRepository {
        return LocalGameRepository(db)
    }


    @Provides
    @ActivityScope
    fun provideBackground(): Background {
        return BgExecutor()
    }

    @Provides
    @ActivityScope
    fun provideForeground(): Foreground {
        return FgExecutor()
    }
}