package nl.entreco.dartsscorecard.di.archive

import dagger.Module
import dagger.Provides
import nl.entreco.data.archive.ArchiveServiceRepository
import nl.entreco.domain.repository.ArchiveRepository


@Module
class ArchiveModule {

    @Provides
    @ArchiveScope
    fun provideArchiveRepository(): ArchiveRepository {
        return ArchiveServiceRepository()
    }
}