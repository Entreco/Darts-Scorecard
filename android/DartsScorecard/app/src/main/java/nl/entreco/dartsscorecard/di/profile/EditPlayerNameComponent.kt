package nl.entreco.dartsscorecard.di.profile

import dagger.Subcomponent
import nl.entreco.dartsscorecard.profile.edit.EditPlayerNameViewModel

/**
 * Created by entreco on 02/03/2018.
 */
@ProfileScope
@Subcomponent(modules = [(EditPlayerNameModule::class)])
interface EditPlayerNameComponent {
    fun viewModel() : EditPlayerNameViewModel
}
