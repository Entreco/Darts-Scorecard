package nl.entreco.dartsscorecard.di.profile

import dagger.Subcomponent
import nl.entreco.dartsscorecard.profile.ProfileViewModel

/**
 * Created by entreco on 21/02/2018.
 */
@ProfileScope
@Subcomponent(modules = [(ProfileModule::class)])
interface ProfileComponent {
    fun viewModel(): ProfileViewModel
}
