package nl.entreco.dartsscorecard.di.profile

import dagger.Subcomponent
import nl.entreco.dartsscorecard.profile.create.CreateProfileViewModel

/**
 * Created by entreco on 20/03/2018.
 */
@ProfileScope
@Subcomponent(modules = [(CreateProfileModule::class)])
interface CreateProfileComponent {
    fun viewModel() : CreateProfileViewModel
}