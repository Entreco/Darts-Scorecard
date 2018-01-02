package nl.entreco.dartsscorecard.di.setup

import dagger.Subcomponent
import nl.entreco.dartsscorecard.setup.edit.EditPlayerViewModel

/**
 * Created by Entreco on 02/01/2018.
 */
@EditPlayerScope
@Subcomponent(modules = [(EditPlayerModule::class)])
interface EditPlayerComponent {
    fun viewModel(): EditPlayerViewModel
}