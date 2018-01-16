package nl.entreco.dartsscorecard.di.viewmodel.db

import dagger.Module
import dagger.Provides
import nl.entreco.dartsscorecard.di.viewmodel.ActivityScope
import nl.entreco.data.db.DscDatabase
import nl.entreco.data.db.LocalMetaRepository
import nl.entreco.domain.repository.MetaRepository

/**
 * Created by entreco on 10/01/2018.
 */
@Module
class StatDbModule {
    @Provides
    @ActivityScope
    fun provideStatRepository(db: DscDatabase): MetaRepository {
        return LocalMetaRepository(db)
    }
}