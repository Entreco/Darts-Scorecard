package nl.entreco.dartsscorecard.di.streaming

import dagger.Subcomponent
import nl.entreco.dartsscorecard.base.PermissionHelper
import nl.entreco.dartsscorecard.play.stream.StreamViewModel

@StreamScope
@Subcomponent(modules = [(StreamModule::class)])
interface StreamComponent {
    fun viewModel(): StreamViewModel
    fun permissionHelper(): PermissionHelper
}