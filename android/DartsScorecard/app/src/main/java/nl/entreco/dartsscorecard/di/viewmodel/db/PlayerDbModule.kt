package nl.entreco.dartsscorecard.di.viewmodel.db

import dagger.Module
import dagger.Provides
import nl.entreco.dartsscorecard.di.viewmodel.ActivityScope
import nl.entreco.data.DscDatabase
import nl.entreco.data.db.player.PlayerMapper
import nl.entreco.data.play.repository.LocalPlayerRepository
import nl.entreco.domain.play.repository.PlayerRepository

/**
 * Created by Entreco on 17/12/2017.
 */
@Module
class PlayerDbModule {

    @Provides
    @ActivityScope
    fun providePlayerMapper():PlayerMapper {
        return PlayerMapper()
    }

    @Provides
    @ActivityScope
    fun providePlayerRepository(db: DscDatabase, mapper: PlayerMapper): PlayerRepository {
        return LocalPlayerRepository(db, mapper)
    }

}