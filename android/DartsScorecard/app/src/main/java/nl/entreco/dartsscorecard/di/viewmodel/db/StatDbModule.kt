package nl.entreco.dartsscorecard.di.viewmodel.db

import dagger.Module
import dagger.Provides
import nl.entreco.dartsscorecard.di.viewmodel.ActivityScope
import nl.entreco.data.db.DscDatabase
import nl.entreco.data.db.stats.LocalStatRepository
import nl.entreco.data.db.stats.StatMapper
import nl.entreco.domain.repository.StatRepository

/**
 * Created by entreco on 16/01/2018.
 */
@Module
class StatDbModule {

    @Provides
    @ActivityScope
    fun provideStatMapper(): StatMapper {
        return StatMapper()
    }

    @Provides
    @ActivityScope
    fun provideStatRepository(db: DscDatabase, mapper: StatMapper): StatRepository {
        return LocalStatRepository(db, mapper)
    }
}