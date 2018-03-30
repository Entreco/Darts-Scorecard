package nl.entreco.dartsscorecard.di.profile

import dagger.Subcomponent
import nl.entreco.dartsscorecard.profile.select.SelectProfileViewModel

/**
 * Created by entreco on 04/03/2018.
 */
@Subcomponent(modules = [(SelectProfileModule::class)])
interface SelectProfileComponent {
    fun viewModel(): SelectProfileViewModel
}
