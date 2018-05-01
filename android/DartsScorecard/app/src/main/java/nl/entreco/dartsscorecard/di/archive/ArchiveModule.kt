package nl.entreco.dartsscorecard.di.archive

import dagger.Module
import dagger.Provides
import nl.entreco.data.archive.ArchiveServiceRepository
import nl.entreco.data.db.DscDatabase
import nl.entreco.domain.common.log.Logger
import nl.entreco.domain.repository.ArchiveRepository


@Module
class ArchiveModule {

    @Provides
    @ArchiveScope
    fun provideArchiveRepository(db: DscDatabase, logger: Logger): ArchiveRepository {
        return ArchiveServiceRepository(db, logger)
    }
}