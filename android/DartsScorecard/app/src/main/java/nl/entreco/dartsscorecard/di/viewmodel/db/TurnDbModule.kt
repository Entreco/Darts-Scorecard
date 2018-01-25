package nl.entreco.dartsscorecard.di.viewmodel.db

import dagger.Module
import dagger.Provides
import nl.entreco.dartsscorecard.di.viewmodel.ActivityScope
import nl.entreco.data.db.DscDatabase
import nl.entreco.data.db.turn.LocalTurnRepository
import nl.entreco.data.db.turn.TurnMapper
import nl.entreco.domain.repository.TurnRepository

/**
 * Created by Entreco on 23/12/2017.
 */
@Module
class TurnDbModule {

    @Provides
    @ActivityScope
    fun provideTurnMapper(): TurnMapper {
        return TurnMapper()
    }

    @Provides
    @ActivityScope
    fun provideTurnRepository(db: DscDatabase, mapper: TurnMapper): TurnRepository {
        return LocalTurnRepository(db, mapper)
    }
}