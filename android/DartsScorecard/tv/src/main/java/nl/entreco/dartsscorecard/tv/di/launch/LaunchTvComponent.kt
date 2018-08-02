package nl.entreco.dartsscorecard.tv.di.launch

import dagger.Subcomponent
import nl.entreco.dartsscorecard.tv.launch.LaunchTvViewModel

@LaunchTvScope
@Subcomponent(modules = [LaunchTvModule::class])
interface LaunchTvComponent {
    fun viewModel() : LaunchTvViewModel
}