package nl.entreco.dartsscorecard.di.service

import dagger.Subcomponent
import nl.entreco.dartsscorecard.di.archive.ArchiveComponent
import nl.entreco.dartsscorecard.di.archive.ArchiveModule
import nl.entreco.dartsscorecard.di.viewmodel.api.FeatureApiModule
import nl.entreco.dartsscorecard.di.viewmodel.db.*
import nl.entreco.dartsscorecard.di.viewmodel.threading.ThreadingModule

@ServiceScope
@Subcomponent(modules = [(ServiceModule::class), (ThreadingModule::class)])
interface ServiceComponent {
    fun plus(module: ArchiveModule): ArchiveComponent
}