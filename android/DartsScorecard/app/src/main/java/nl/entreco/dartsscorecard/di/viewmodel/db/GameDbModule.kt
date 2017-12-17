package nl.entreco.dartsscorecard.di.viewmodel.db

import dagger.Module
import dagger.Provides
import nl.entreco.dartsscorecard.di.viewmodel.ActivityScope
import nl.entreco.data.DscDatabase
import nl.entreco.data.db.Mapper
import nl.entreco.data.db.game.GameMapper
import nl.entreco.data.db.game.GameTable
import nl.entreco.data.play.repository.LocalGameRepository
import nl.entreco.domain.play.model.Game
import nl.entreco.domain.play.repository.GameRepository

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