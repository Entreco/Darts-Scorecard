package nl.entreco.dartsscorecard.di.launch

import dagger.Subcomponent
import nl.entreco.dartsscorecard.launch.LaunchViewModel
import nl.entreco.libads.Ads
import nl.entreco.libads.ui.AdViewModel

/**
 * Created by Entreco on 12/12/2017.
 */
@LaunchScope
@Subcomponent(modules = [(LaunchModule::class)])
interface LaunchComponent {
    fun viewModel(): LaunchViewModel
    fun ads(): Ads
    fun adViewModel(): AdViewModel
}