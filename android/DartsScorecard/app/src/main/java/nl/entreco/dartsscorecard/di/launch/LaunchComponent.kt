package nl.entreco.dartsscorecard.di.launch

import dagger.Subcomponent
import nl.entreco.dartsscorecard.launch.LaunchViewModel

/**
 * Created by Entreco on 12/12/2017.
 */
@LaunchScope
@Subcomponent(modules = [(LaunchModule::class)])
interface LaunchComponent {
    fun viewModel(): LaunchViewModel
}