package nl.entreco.dartsscorecard.di.viewmodel.db

import dagger.Module
import dagger.Provides
import nl.entreco.dartsscorecard.di.viewmodel.ActivityScope
import nl.entreco.data.db.DscDatabase
import nl.entreco.data.db.game.GameMapper
import nl.entreco.data.db.game.LocalGameRepository
import nl.entreco.domain.repository.GameRepository

/**
 * Created by Entreco on 17/12/2017.
 */
@Module
class GameDbModule {

    @Provides
    @ActivityScope
    fun provideGameMapper(): GameMapper {
        return GameMapper()
    }

    @Provides
    @ActivityScope
    fun provideGameRepository(db: DscDatabase, mapper: GameMapper): GameRepository {
        return LocalGameRepository(db, mapper)
    }
}