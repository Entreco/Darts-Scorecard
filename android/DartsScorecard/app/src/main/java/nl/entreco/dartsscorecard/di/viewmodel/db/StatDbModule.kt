package nl.entreco.dartsscorecard.di.viewmodel.db

import dagger.Module
import dagger.Provides
import nl.entreco.libcore.scopes.ActivityScope
import nl.entreco.data.db.DscDatabase
import nl.entreco.data.db.stats.LocalLiveStatRepository
import nl.entreco.data.db.stats.LiveStatMapper
import nl.entreco.domain.repository.LiveStatRepository

/**
 * Created by entreco on 16/01/2018.
 */
@Module
class StatDbModule {

    @Provides
    @ActivityScope
    fun provideStatMapper(): LiveStatMapper {
        return LiveStatMapper()
    }

    @Provides
    @ActivityScope
    fun provideStatRepository(db: DscDatabase, mapperLive: LiveStatMapper): LiveStatRepository {
        return LocalLiveStatRepository(db, mapperLive)
    }
}