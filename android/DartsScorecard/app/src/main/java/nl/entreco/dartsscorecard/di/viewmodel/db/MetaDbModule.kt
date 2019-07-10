package nl.entreco.dartsscorecard.di.viewmodel.db

import dagger.Module
import dagger.Provides
import nl.entreco.shared.scopes.ActivityScope
import nl.entreco.data.db.DscDatabase
import nl.entreco.data.db.meta.LocalMetaRepository
import nl.entreco.data.db.meta.MetaMapper
import nl.entreco.domain.repository.MetaRepository

/**
 * Created by entreco on 16/01/2018.
 */
@Module
class MetaDbModule {

    @Provides
    @ActivityScope
    fun provideMetaMapper(): MetaMapper {
        return MetaMapper()
    }

    @Provides
    @ActivityScope
    fun provideMetaRepository(db: DscDatabase, mapper: MetaMapper): MetaRepository {
        return LocalMetaRepository(db, mapper)
    }
}