package nl.entreco.dartsscorecard.di.tv

import dagger.Subcomponent
import nl.entreco.dartsscorecard.tv.launch.LaunchTvNavigator
import nl.entreco.dartsscorecard.tv.launch.LaunchTvViewModel

@TvScope
@Subcomponent(modules = [(TvLaunchModule::class)])
interface TvLaunchComponent {
    fun viewModel(): LaunchTvViewModel
    fun navigator(): LaunchTvNavigator
}