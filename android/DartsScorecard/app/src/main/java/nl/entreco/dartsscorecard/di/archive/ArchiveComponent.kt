package nl.entreco.dartsscorecard.di.archive

import dagger.Subcomponent
import nl.entreco.domain.profile.archive.ArchiveStatsUsecase

@ArchiveScope
@Subcomponent(modules = [(ArchiveModule::class)])
interface ArchiveComponent {
    fun archive(): ArchiveStatsUsecase
}