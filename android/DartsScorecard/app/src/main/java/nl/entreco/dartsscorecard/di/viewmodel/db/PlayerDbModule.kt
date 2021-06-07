package nl.entreco.dartsscorecard.di.viewmodel.db

import dagger.Module
import dagger.Provides
import nl.entreco.data.db.DscDatabase
import nl.entreco.data.db.player.LocalPlayerRepository
import nl.entreco.data.db.player.PlayerMapper
import nl.entreco.domain.repository.PlayerRepository
import nl.entreco.libcore.scopes.ActivityScope

/**
 * Created by Entreco on 17/12/2017.
 */
@Module
class PlayerDbModule {

    @Provides
    @ActivityScope
    fun providePlayerMapper(): PlayerMapper {
        return PlayerMapper()
    }

    @Provides
    @ActivityScope
    fun providePlayerRepository(db: DscDatabase, mapper: PlayerMapper): PlayerRepository {
        return LocalPlayerRepository(db, mapper)
    }
}