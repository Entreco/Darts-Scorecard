package nl.entreco.dartsscorecard.di.archive

import dagger.Module
import dagger.Provides
import nl.entreco.data.db.DscDatabase
import nl.entreco.data.db.profile.ArchiveServiceRepository
import nl.entreco.data.db.profile.ArchiveStatMapper
import nl.entreco.domain.common.log.Logger
import nl.entreco.domain.repository.ArchiveRepository


@Module
class ArchiveModule {

    @Provides
    @ArchiveScope
    fun provideArchiveStatMapper(): ArchiveStatMapper {
        return ArchiveStatMapper()
    }

    @Provides
    @ArchiveScope
    fun provideArchiveRepository(db: DscDatabase, mapper: ArchiveStatMapper, logger: Logger): ArchiveRepository {
        return ArchiveServiceRepository(db, mapper, logger)
    }
}