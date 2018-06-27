package nl.entreco.dartsscorecard.di.profile

import dagger.Subcomponent
import nl.entreco.dartsscorecard.ad.AdViewModel
import nl.entreco.dartsscorecard.profile.view.ProfileViewModel

/**
 * Created by entreco on 21/02/2018.
 */
@ProfileScope
@Subcomponent(modules = [(ProfileModule::class)])
interface ProfileComponent {
    fun viewModel(): ProfileViewModel
    fun ads(): AdViewModel
}
