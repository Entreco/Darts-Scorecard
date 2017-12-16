package nl.entreco.dartsscorecard.splash

import dagger.Module
import dagger.Provides
import nl.entreco.dartsscorecard.di.viewmodel.ActivityScope
import nl.entreco.data.DscDatabase
import nl.entreco.data.db.game.GameMapper
import nl.entreco.data.db.player.PlayerMapper
import nl.entreco.data.play.repository.LocalGameRepository
import nl.entreco.data.play.repository.LocalPlayerRepository
import nl.entreco.domain.executors.Background
import nl.entreco.domain.executors.BgExecutor
import nl.entreco.domain.executors.FgExecutor
import nl.entreco.domain.executors.Foreground
import nl.entreco.domain.play.repository.GameRepository
import nl.entreco.domain.play.repository.PlayerRepository

/**
 * Created by Entreco on 12/12/2017.
 */
@Module
class SplashModule {


    @Provides
    @ActivityScope
    fun provideGameMapper(): GameMapper {
        return GameMapper()
    }

    @Provides
    @ActivityScope
    fun providePlayerMapper(): PlayerMapper {
        return PlayerMapper()
    }

    @Provides
    @ActivityScope
    fun provideGameRepository(db: DscDatabase, mapper: GameMapper): GameRepository {
        return LocalGameRepository(db, mapper)
    }

    @Provides
    @ActivityScope
    fun providePlayerRepository(db: DscDatabase, mapper: PlayerMapper): PlayerRepository {
        return LocalPlayerRepository(db, mapper)
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